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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ATMAccess
 */
@JsonPropertyOrder({
        ATMAccess.JSON_PROPERTY_MODE,
        ATMAccess.JSON_PROPERTY_SCHEDULE
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ATMAccess {
    /**
     * Доступность АТМ для клиента, принимает следующие значения:  alltime &#x3D; круглосуточно  schedule &#x3D; по расписанию работы организации. В этом случае расписание указывается в поле schedule.
     */
    public enum ModeEnum {
        ALLTIME("alltime"),

        SCHEDULE("schedule"),

        NOINFO("noinfo");

        private String value;

        ModeEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static ModeEnum fromValue(String value) {
            for (ModeEnum b : ModeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_MODE = "mode";
    private ModeEnum mode;

    public static final String JSON_PROPERTY_SCHEDULE = "schedule";
    private String schedule;

}

