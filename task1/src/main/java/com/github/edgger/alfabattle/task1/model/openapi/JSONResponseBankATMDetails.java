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

/**
 * JSONResponseBankATMDetails
 */
@JsonPropertyOrder({
        JSONResponseBankATMDetails.JSON_PROPERTY_DATA,
        JSONResponseBankATMDetails.JSON_PROPERTY_ERROR,
        JSONResponseBankATMDetails.JSON_PROPERTY_SUCCESS,
        JSONResponseBankATMDetails.JSON_PROPERTY_TOTAL
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JSONResponseBankATMDetails {
    public static final String JSON_PROPERTY_DATA = "data";
    private BankATMDetails data;

    public static final String JSON_PROPERTY_ERROR = "error";
    private Error error;

    public static final String JSON_PROPERTY_SUCCESS = "success";
    private Boolean success;

    public static final String JSON_PROPERTY_TOTAL = "total";
    private Integer total;

}

