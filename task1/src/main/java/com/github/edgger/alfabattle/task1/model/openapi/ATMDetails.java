/*
 * Сервис проверки статуса банкоматов
 * Сервис, возвращающий информацию о банкоматах Альфа-Банка
 *
 * The version of the OpenAPI document: 1.0.0
 * Contact: apisupport@alfabank.ru
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.github.edgger.alfabattle.task1.model.openapi;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Статическая и редкоменяющаяся информация о банкомате
 */
@JsonPropertyOrder({
        ATMDetails.JSON_PROPERTY_ADDRESS,
        ATMDetails.JSON_PROPERTY_ADDRESS_COMMENTS,
        ATMDetails.JSON_PROPERTY_AVAILABLE_PAYMENT_SYSTEMS,
        ATMDetails.JSON_PROPERTY_CASH_IN_CURRENCIES,
        ATMDetails.JSON_PROPERTY_CASH_OUT_CURRENCIES,
        ATMDetails.JSON_PROPERTY_COORDINATES,
        ATMDetails.JSON_PROPERTY_DEVICE_ID,
        ATMDetails.JSON_PROPERTY_NFC,
        ATMDetails.JSON_PROPERTY_PUBLIC_ACCESS,
        ATMDetails.JSON_PROPERTY_RECORD_UPDATED,
        ATMDetails.JSON_PROPERTY_SERVICES,
        ATMDetails.JSON_PROPERTY_SUPPORT_INFO,
        ATMDetails.JSON_PROPERTY_TIME_ACCESS,
        ATMDetails.JSON_PROPERTY_TIME_SHIFT
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ATMDetails {
    public static final String JSON_PROPERTY_ADDRESS = "address";
    private PostAddress address;

    public static final String JSON_PROPERTY_ADDRESS_COMMENTS = "addressComments";
    private String addressComments;

    public static final String JSON_PROPERTY_AVAILABLE_PAYMENT_SYSTEMS = "availablePaymentSystems";
    private List<String> availablePaymentSystems = null;

    public static final String JSON_PROPERTY_CASH_IN_CURRENCIES = "cashInCurrencies";
    private List<String> cashInCurrencies = null;

    public static final String JSON_PROPERTY_CASH_OUT_CURRENCIES = "cashOutCurrencies";
    private List<String> cashOutCurrencies = null;

    public static final String JSON_PROPERTY_COORDINATES = "coordinates";
    private Coordinates coordinates;

    public static final String JSON_PROPERTY_DEVICE_ID = "deviceId";
    private Integer deviceId;

    public static final String JSON_PROPERTY_NFC = "nfc";
    private String nfc;

    public static final String JSON_PROPERTY_PUBLIC_ACCESS = "publicAccess";
    private String publicAccess;

    public static final String JSON_PROPERTY_RECORD_UPDATED = "recordUpdated";
    private OffsetDateTime recordUpdated;

    public static final String JSON_PROPERTY_SERVICES = "services";
    private ATMServices services;

    public static final String JSON_PROPERTY_SUPPORT_INFO = "supportInfo";
    private SupportInfo supportInfo;

    public static final String JSON_PROPERTY_TIME_ACCESS = "timeAccess";
    private ATMAccess timeAccess;

    public static final String JSON_PROPERTY_TIME_SHIFT = "timeShift";
    private Integer timeShift;

}

