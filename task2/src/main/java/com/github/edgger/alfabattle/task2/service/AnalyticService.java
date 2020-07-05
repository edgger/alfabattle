package com.github.edgger.alfabattle.task2.service;

import com.github.edgger.alfabattle.task2.exceptions.UserNotFoundException;
import com.github.edgger.alfabattle.task2.model.*;
import com.github.edgger.alfabattle.task2.util.RawDataConsumer;
import com.github.edgger.alfabattle.task2.util.RawDataProducer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AnalyticService {

    private final RawDataConsumer rawDataConsumer;
    private final RawDataProducer rawDataProducer;

    @Autowired
    public AnalyticService(RawDataConsumer rawDataConsumer, RawDataProducer rawDataProducer) {
        this.rawDataConsumer = rawDataConsumer;
        this.rawDataProducer = rawDataProducer;
    }

    public Collection<UserPaymentAnalytic> getAnalytic() {
        return rawDataConsumer.getRawDataList().stream()
                .collect(Collectors.toMap(RawData::getUserId,
                        this::mapToUserPaymentAnalytic,
                        this::mergeUserPaymentAnalytic))
                .values();
    }

    public UserPaymentAnalytic getAnalyticByUserId(String userId) {
        return rawDataConsumer.getRawDataList().stream()
                .filter(rawData -> userId.equals(rawData.getUserId()))
                .map(this::mapToUserPaymentAnalytic)
                .reduce(this::mergeUserPaymentAnalytic)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserPaymentStats getStatsByUserId(String userId) {
        Map<Integer, CategoryStat> categoryIdStats = rawDataConsumer.getRawDataList().stream()
                .filter(rawData -> userId.equals(rawData.getUserId()))
                .collect(Collectors.toMap(RawData::getCategoryId,
                        rawData -> new CategoryStat(rawData.getAmount().intValue()),
                        (stat, stat2) -> stat.increase(stat2.getAmount())));

        if (categoryIdStats.isEmpty()) {
            throw new UserNotFoundException();
        }

        Map.Entry<Integer, CategoryStat> firstEntry = categoryIdStats.entrySet().iterator().next();
        Integer firstCategoryId = firstEntry.getKey();
        UserPaymentStats result = new UserPaymentStats(firstCategoryId, firstCategoryId, firstCategoryId, firstCategoryId);
        CategoryStat firstCategoryStat = firstEntry.getValue();
        int minAmount = firstCategoryStat.getAmount();
        int maxAmount = firstCategoryStat.getAmount();
        int minFrequency = firstCategoryStat.getFrequency();
        int maxFrequency = firstCategoryStat.getFrequency();

        for (Map.Entry<Integer, CategoryStat> entry : categoryIdStats.entrySet()) {
            CategoryStat statsData = entry.getValue();
            Integer categoryId = entry.getKey();
            if (minAmount >= statsData.getAmount()) {
                minAmount = statsData.getAmount();
                result.setMinAmountCategoryId(categoryId);
            }
            if (maxAmount <= statsData.getAmount()) {
                maxAmount = statsData.getAmount();
                result.setMaxAmountCategoryId(categoryId);
            }
            if (minFrequency >= statsData.getFrequency()) {
                minFrequency = statsData.getFrequency();
                result.setRareCategoryId(categoryId);
            }
            if (maxFrequency <= statsData.getFrequency()) {
                maxFrequency = statsData.getFrequency();
                result.setOftenCategoryId(categoryId);
            }
        }

        return result;
    }

    public Collection<UserTemplate> getTemplatesByUserId(String userId) {
        Map<TemplateData, Integer> templateDataNumber = rawDataConsumer.getRawDataList().stream()
                .filter(rawData -> userId.equals(rawData.getUserId()))
                .map(this::mapToTemplateData)
                .collect(Collectors.toMap(templateData -> templateData, templateData -> 1, Integer::sum));

        if (templateDataNumber.isEmpty()) {
            throw new UserNotFoundException();
        }

        return templateDataNumber.entrySet().stream()
                .filter(entry -> entry.getValue() >= 3)
                .map(Map.Entry::getKey)
                .map(this::mapToUserTemplate)
                .collect(Collectors.toList());
    }

    private UserPaymentAnalytic mapToUserPaymentAnalytic(RawData rawData) {
        HashMap<String, PaymentCategoryInfo> analyticInfo = new HashMap<>();
        PaymentCategoryInfo paymentCategoryInfo = new PaymentCategoryInfo(rawData.getAmount(), rawData.getAmount(), rawData.getAmount());
        analyticInfo.put(rawData.getCategoryId().toString(), paymentCategoryInfo);
        return new UserPaymentAnalytic(rawData.getUserId(), rawData.getAmount(), analyticInfo);
    }

    private UserPaymentAnalytic mergeUserPaymentAnalytic(UserPaymentAnalytic upa, UserPaymentAnalytic upa2) {
        if (!Objects.equals(upa.getUserId(), upa2.getUserId())) {
            throw new IllegalArgumentException("Different UserPaymentAnalytics");
        }
        Map<String, PaymentCategoryInfo> analyticInfo = mergeAnalyticInfo(upa.getAnalyticInfo(), upa2.getAnalyticInfo());

        UserPaymentAnalytic result = new UserPaymentAnalytic();
        result.setUserId(upa.getUserId());
        result.setAnalyticInfo(analyticInfo);
        result.setTotalSum(analyticInfo.values().stream().mapToDouble(PaymentCategoryInfo::getSum).sum());
        return result;
    }

    private Map<String, PaymentCategoryInfo> mergeAnalyticInfo(Map<String, PaymentCategoryInfo> analyticInfo,
                                                               Map<String, PaymentCategoryInfo> analyticInfo2) {
        HashMap<String, PaymentCategoryInfo> result = new HashMap<>(analyticInfo);
        analyticInfo2.forEach((pciId, pci) -> result.merge(pciId, pci, this::mergePaymentCategoryInfos));
        return result;
    }

    private PaymentCategoryInfo mergePaymentCategoryInfos(PaymentCategoryInfo pci, PaymentCategoryInfo pci2) {
        PaymentCategoryInfo result = new PaymentCategoryInfo();
        result.setMin(Math.min(pci.getMin(), pci2.getMin()));
        result.setMax(Math.max(pci.getMax(), pci2.getMax()));
        result.setSum(pci.getSum() + pci2.getSum());
        return result;
    }

    public void addAnalytic() {
        rawDataProducer.produce();
    }

    @Data
    private static class CategoryStat {
        private int frequency = 1;
        private int amount;

        public CategoryStat(int amount) {
            this.amount = amount;
        }

        public CategoryStat increase(int amount) {
            this.amount = this.amount + amount;
            this.frequency++;
            return this;
        }
    }

    private TemplateData mapToTemplateData(RawData rawData) {
        return new TemplateData(rawData.getUserId(), rawData.getRecipientId(), rawData.getCategoryId(), rawData.getAmount());
    }

    private UserTemplate mapToUserTemplate(TemplateData templateData) {
        return new UserTemplate(templateData.getAmount(), templateData.getCategoryId(), templateData.getRecipientId());
    }

    @Data
    @AllArgsConstructor
    private static class TemplateData {
        private String userId;
        private String recipientId;
        private int categoryId;
        private double amount;
    }
}
