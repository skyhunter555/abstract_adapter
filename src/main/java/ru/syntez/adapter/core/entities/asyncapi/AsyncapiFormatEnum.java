package ru.syntez.adapter.core.entities.asyncapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * https://www.asyncapi.com/docs/specifications/v2.3.0-2022-01-release.2#dataTypeFormat
 *
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
 *
 * Asyncapi data format
 *
 * @author Skyhunter
 * @date 19.01.2022
 */
public enum AsyncapiFormatEnum {

    INT32   (AsyncapiTypeEnum.INTEGER,"int32"),
    INT64   (AsyncapiTypeEnum.INTEGER,"int64"),
    FLOAT   (AsyncapiTypeEnum.NUMBER, "float"),
    DOUBLE  (AsyncapiTypeEnum.NUMBER, "double"),
    BYTE    (AsyncapiTypeEnum.STRING, "byte"),
    BINARY  (AsyncapiTypeEnum.STRING, "binary"),
    DATE    (AsyncapiTypeEnum.STRING, "date"),
    DATETIME(AsyncapiTypeEnum.STRING, "date-time"),
    PASSWORD(AsyncapiTypeEnum.STRING, "password"),
    EMAIL   (AsyncapiTypeEnum.STRING, "email");

    private final AsyncapiTypeEnum type;

    @JsonProperty("format")
    private final String code;

    AsyncapiFormatEnum(AsyncapiTypeEnum type, String code) {
        this.type = type;
        this.code = code;
    }

    public AsyncapiTypeEnum getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AsyncapiFormatEnum forValues(@JsonProperty("format") String code) {
        for (AsyncapiFormatEnum each : values()) {
            if (each.code.equals(code)) {
                return each;
            }
        }
        return null;
    }

}
