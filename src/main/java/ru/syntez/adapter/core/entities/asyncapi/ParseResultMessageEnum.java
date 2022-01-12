package ru.syntez.adapter.core.entities.asyncapi;

/**
 * Parse result message
 *
 * @author Skyhunter
 * @date 24.12.2021
 */
public enum ParseResultMessageEnum {

    OK("OK"),
    EMPTY_FIELD("Field '%s' value is empty."),
    NOT_DIGIT("Field '%s' value '%s' is not digit [0-9] or empty."),
    INVALID_SCHEMA("Count of fields '%s' does not match '%s'.");

    private final String description;

    ParseResultMessageEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
