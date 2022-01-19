package ru.syntez.adapter.core.entities.asyncapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.syntez.adapter.core.entities.readfile.FileExtensionEnum;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;

/**
 * https://www.asyncapi.com/docs/specifications/v2.3.0-2022-01-release.2#dataTypeFormat
 * <p>
 * Common Name	type	format	Comments
 * integer	integer	int32	signed 32 bits
 * long	    integer	int64	signed 64 bits
 * float	number	float
 * double	number	double
 * string	string
 * byte	    string	byte	base64 encoded characters
 * binary	string	binary	any sequence of octets
 * boolean	boolean
 * date	    string	date	As defined by full-date - RFC3339
 * dateTime	string	date-time	As defined by date-time - RFC3339
 * password	string	password	Used to hint UIs the input needs to be obscured.
 * <p>
 * Asyncapi Type
 *
 * @author Skyhunter
 * @date 24.12.2022
 */
public enum AsyncapiTypeEnum {

    BOOLEAN("boolean"),
    OBJECT( "object"),
    INTEGER("integer"),
    NUMBER( "number"),
    STRING( "string");

    @JsonProperty("type")
    private final String code;

    AsyncapiTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AsyncapiTypeEnum forValues(@JsonProperty("type") String code) {
        for (AsyncapiTypeEnum each : values()) {
            if (each.code.equals(code)) {
                return each;
            }
        }
        return null;
    }
}
