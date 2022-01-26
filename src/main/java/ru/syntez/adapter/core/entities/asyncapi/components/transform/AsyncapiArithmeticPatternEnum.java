package ru.syntez.adapter.core.entities.asyncapi.components.transform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Арифметические шаблоны трансформации
 *
 * @author Skyhunter
 * @date 26.01.2022
 */
public enum AsyncapiArithmeticPatternEnum {

    SUM("sum"),
    COUNT( "count"),
    AVG("avg");

    @JsonProperty("type")
    private final String code;

    AsyncapiArithmeticPatternEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AsyncapiArithmeticPatternEnum forValues(@JsonProperty("type") String code) {
        for (AsyncapiArithmeticPatternEnum each : values()) {
            if (each.code.equals(code)) {
                return each;
            }
        }
        return null;
    }
}
