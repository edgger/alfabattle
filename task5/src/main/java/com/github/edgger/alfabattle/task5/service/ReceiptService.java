package com.github.edgger.alfabattle.task5.service;

import com.github.edgger.alfabattle.task5.dto.*;
import com.github.edgger.alfabattle.task5.model.Group;
import com.github.edgger.alfabattle.task5.model.Item;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReceiptService {

    private static final Integer RULE_LOCAL_FACTOR = 0;
    private static final Integer RULE_RETAIL_FACTOR = 1;

    private final PromoService promoService;
    private final Map<String, Item> itemMap;
    private final Map<String, Group> groupMap;

    @Autowired
    public ReceiptService(PromoService promoService) throws IOException {
        this.promoService = promoService;
        this.itemMap = loadItemMap();
        this.groupMap = loadGroupMap();
    }

    private Map<String, Item> loadItemMap() throws IOException {
        ClassPathResource itemsResource = new ClassPathResource("items.csv");
        try (Reader itemsReader = new InputStreamReader(itemsResource.getInputStream())) {
            return new CsvToBeanBuilder<Item>(itemsReader).withType(Item.class)
                    .build()
                    .parse()
                    .stream()
                    .collect(Collectors.toMap(Item::getId, Function.identity()));
        }
    }

    private Map<String, Group> loadGroupMap() throws IOException {
        ClassPathResource itemsResource = new ClassPathResource("groups.csv");
        try (Reader groupsReader = new InputStreamReader(itemsResource.getInputStream())) {
            return new CsvToBeanBuilder<Group>(groupsReader)
                    .withType(Group.class)
                    .build()
                    .parse()
                    .stream()
                    .collect(Collectors.toMap(Group::getId, Function.identity()));
        }
    }

    public FinalPriceReceipt getFinalPriceReceipt(ShoppingCart shoppingCart) {
        Map<Item, TreeMap<Integer, BigDecimal>> itemDiscountsMap = getItemDiscountsMap(shoppingCart);

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        List<FinalPricePosition> positions = new ArrayList<>(shoppingCart.getPositions().size());

        for (Map.Entry<Item, TreeMap<Integer, BigDecimal>> itemDiscountsEntry : itemDiscountsMap.entrySet()) {
            Item item = itemDiscountsEntry.getKey();
            BigDecimal finalPrice = item.getPrice();

            TreeMap<Integer, BigDecimal> discountsMap = itemDiscountsEntry.getValue();
            if (discountsMap != null) {
                Map.Entry<Integer, BigDecimal> bestRuleEntry = discountsMap.firstEntry();
                if (bestRuleEntry != null) {
                    finalPrice = bestRuleEntry.getValue();
                }
            }

            total = total.add(item.getPrice());
            discount = discount.add(item.getPrice().subtract(finalPrice));
            positions.add(new FinalPricePosition(
                    item.getId(),
                    item.getName(),
                    finalPrice,
                    item.getPrice()
            ));
        }

        return new FinalPriceReceipt(total, discount, positions);
    }

    private Map<Item, TreeMap<Integer, BigDecimal>> getItemDiscountsMap(ShoppingCart shoppingCart) {
        Map<Item, TreeMap<Integer, BigDecimal>> itemDiscountsMap = new HashMap<>();
        Map<String, Integer> groupCountMap = new HashMap<>();

        LoyaltyCardRule localCardRule = getLocalCardRule(shoppingCart);
        LoyaltyCardRule retailCardRule = getRetailCardRule(shoppingCart);

        for (ItemPosition position : shoppingCart.getPositions()) {
            Item item = itemMap.get(position.getItemId());

            TreeMap<Integer, BigDecimal> discounts = new TreeMap<>();

            addCardDiscount(discounts, localCardRule, retailCardRule, item);

            addCountDiscount(discounts, shoppingCart, position);

            groupCountMap.merge(item.getGroupId(), 1, Integer::sum);

            itemDiscountsMap.put(item, discounts);
        }

        addGroupDiscount(itemDiscountsMap, groupCountMap, shoppingCart);

        return itemDiscountsMap;
    }

    private LoyaltyCardRule getRetailCardRule(ShoppingCart shoppingCart) {
        PromoMatrix promoMatrix = promoService.getPromoMatrix();
        List<LoyaltyCardRule> loyaltyCardRules = promoMatrix.getLoyaltyCardRules();
        return loyaltyCardRules.stream()
                .filter(rule -> shoppingCart.isLoyaltyCard())
                .filter(rule -> rule.getShopId() < 0)
                .findFirst()
                .orElse(null);
    }

    private LoyaltyCardRule getLocalCardRule(ShoppingCart shoppingCart) {
        PromoMatrix promoMatrix = promoService.getPromoMatrix();
        List<LoyaltyCardRule> loyaltyCardRules = promoMatrix.getLoyaltyCardRules();
        return loyaltyCardRules.stream()
                .filter(rule -> shoppingCart.isLoyaltyCard())
                .filter(rule -> rule.getShopId() == shoppingCart.getShopId())
                .findFirst()
                .orElse(null);
    }

    private void addCardDiscount(TreeMap<Integer, BigDecimal> discounts, LoyaltyCardRule localCardRule,
                                 LoyaltyCardRule retailCardRule,
                                 Item item) {
        if (localCardRule != null) {
            discounts.compute(RULE_LOCAL_FACTOR, remapCardRule(item, localCardRule));
        }

        if (retailCardRule != null) {
            discounts.compute(RULE_RETAIL_FACTOR, remapCardRule(item, retailCardRule));
        }
    }

    private void addCountDiscount(TreeMap<Integer, BigDecimal> discounts, ShoppingCart shoppingCart,
                                  ItemPosition position) {
        PromoMatrix promoMatrix = promoService.getPromoMatrix();
        Item item = itemMap.get(position.getItemId());
        List<ItemCountRule> itemCountRules = promoMatrix.getItemCountRules();

        itemCountRules.stream()
                .filter(rule -> rule.getItemId().equals(item.getId()))
                .filter(rule -> rule.getTriggerQuantity() + rule.getBonusQuantity() <= position.getQuantity())
                .filter(rule -> rule.getShopId() == shoppingCart.getShopId())
                .findFirst()
                .ifPresent(countRule -> discounts.
                        compute(RULE_LOCAL_FACTOR, remapCountRule(position, item, countRule)));

        itemCountRules.stream()
                .filter(rule -> rule.getItemId().equals(item.getId()))
                .filter(rule -> rule.getTriggerQuantity() + rule.getBonusQuantity() <= position.getQuantity())
                .filter(rule -> rule.getShopId() < 0)
                .findFirst()
                .ifPresent(countRule -> discounts
                        .compute(RULE_RETAIL_FACTOR, remapCountRule(position, item, countRule)));
    }

    private void addGroupDiscount(Map<Item, TreeMap<Integer, BigDecimal>> itemDiscountsMap, Map<String, Integer> groupCountMap, ShoppingCart shoppingCart) {
        PromoMatrix promoMatrix = promoService.getPromoMatrix();
        List<ItemGroupRule> itemGroupRules = promoMatrix.getItemGroupRules();

        itemGroupRules.stream()
                .filter(rule -> rule.getShopId() == shoppingCart.getShopId())
                .filter(rule -> groupCountMap.get(rule.getGroupId()) > 1)
                .forEach(groupRule -> itemDiscountsMap.entrySet().stream()
                        .filter(itemRulesEntry -> groupRule.getGroupId().equals(itemRulesEntry.getKey().getGroupId()))
                        .forEach(itemRulesEntry -> itemRulesEntry.getValue()
                                .compute(RULE_LOCAL_FACTOR, remapGroupRule(groupRule, itemRulesEntry))));
        itemGroupRules.stream()
                .filter(rule -> rule.getShopId() < 0)
                .filter(rule -> groupCountMap.get(rule.getGroupId()) > 1)
                .forEach(groupRule -> itemDiscountsMap.entrySet().stream()
                        .filter(itemRulesEntry -> groupRule.getGroupId().equals(itemRulesEntry.getKey().getGroupId()))
                        .forEach(itemRulesEntry -> itemRulesEntry.getValue()
                                .compute(RULE_RETAIL_FACTOR, remapGroupRule(groupRule, itemRulesEntry))));
    }

    private BiFunction<Integer, BigDecimal, BigDecimal> remapCardRule(
            Item item,
            LoyaltyCardRule cardRule) {
        return (factor, currentDiscount) -> {
            BigDecimal cardDiscount = item.getPrice()
                    .subtract(item.getPrice().multiply(BigDecimal.valueOf(cardRule.getDiscount())))
                    .setScale(2, RoundingMode.HALF_EVEN);
            if (currentDiscount == null || currentDiscount.compareTo(cardDiscount) > 0) {
                return cardDiscount;
            } else {
                return currentDiscount;
            }
        };
    }

    private BiFunction<Integer, BigDecimal, BigDecimal> remapCountRule(
            ItemPosition position,
            Item item,
            ItemCountRule countRule) {
        return (factor, currentDiscount) -> {
            long freeGoods = position.getQuantity() / (countRule.getTriggerQuantity() + countRule.getBonusQuantity());
            BigDecimal countDiscount = item.getPrice()
                    .multiply(BigDecimal.valueOf(position.getQuantity() - freeGoods))
                    .divide(BigDecimal.valueOf(position.getQuantity()), RoundingMode.HALF_EVEN)
                    .setScale(2, RoundingMode.HALF_EVEN);
            if (currentDiscount == null || currentDiscount.compareTo(countDiscount) > 0) {
                return countDiscount;
            } else {
                return currentDiscount;
            }
        };
    }

    private BiFunction<Integer, BigDecimal, BigDecimal> remapGroupRule(
            ItemGroupRule groupRule,
            Map.Entry<Item, TreeMap<Integer, BigDecimal>> itemRulesEntry) {
        return (factor, currentDiscount) -> {
            BigDecimal itemPrice = itemRulesEntry.getKey().getPrice();
            BigDecimal groupDiscount = itemPrice
                    .subtract(itemPrice.multiply(BigDecimal.valueOf(groupRule.getDiscount())))
                    .setScale(2, RoundingMode.HALF_EVEN);
            if (currentDiscount == null || currentDiscount.compareTo(groupDiscount) > 0) {
                return groupDiscount;
            } else {
                return currentDiscount;
            }
        };
    }
}
