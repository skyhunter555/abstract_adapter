package ru.syntez.adapter.core.utils;

import ru.syntez.adapter.core.entities.asyncapi.ParseResultMessageEnum;
import ru.syntez.adapter.core.entities.asyncapi.ValidatedResult;

import java.util.Optional;

public class AsyncapiValidator {

    /**
     * Parse String object value
     * @param recordValue
     * @return result with String
     */
    public static ValidatedResult validateResultString(final String recordValue) {
        return new ValidatedResult(ParseResultMessageEnum.OK.getDescription(), recordValue);
    }
    /**
     * Parse Integer object value
     * @param recordValue
     * @return result with Integer
     */
    public static ValidatedResult validateResultInteger(final String recordValue) {
        Optional<Integer> result = Optional.ofNullable(recordValue)
                .filter(AsyncapiValidator::isDigit)
                .map(Integer::parseInt);
        return new ValidatedResult(String.format(ParseResultMessageEnum.NOT_DIGIT.getDescription(), recordValue));
    }
    /**
     * Check is digit
     * @param str
     * @return
     */
    public static boolean isDigit(final String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return str.chars().allMatch(Character::isDigit);
    }
}
