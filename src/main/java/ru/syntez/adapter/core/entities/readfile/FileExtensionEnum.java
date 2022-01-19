package ru.syntez.adapter.core.entities.readfile;

import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
/**
 * File extensions
 *
 * @author Skyhunter
 * @date 24.12.2021
 */
public enum FileExtensionEnum {
    
    YML("yml"),
    YAML("yaml"),
    JSON("json");

    private final String code;

    FileExtensionEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static FileExtensionEnum parseCode(String code) {
        for (FileExtensionEnum each : values()) {
            if (each.code.equals(code)) {
                return each;
            }
        }
        throw new AsyncapiParserException(String.format("Extension %s not found.", code));
    }
}
