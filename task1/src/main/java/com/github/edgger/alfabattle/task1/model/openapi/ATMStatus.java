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

/**
 * Информация по доступности функций АТМ
 */
@JsonPropertyOrder({
        ATMStatus.JSON_PROPERTY_AVAILABLE_NOW,
        ATMStatus.JSON_PROPERTY_DEVICE_ID,
        ATMStatus.JSON_PROPERTY_RECORD_UPDATED
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ATMStatus {
    public static final String JSON_PROPERTY_AVAILABLE_NOW = "availableNow";
    private AvailableNow availableNow;

    public static final String JSON_PROPERTY_DEVICE_ID = "deviceId";
    private Integer deviceId;

    public static final String JSON_PROPERTY_RECORD_UPDATED = "recordUpdated";
    private OffsetDateTime recordUpdated;

}

