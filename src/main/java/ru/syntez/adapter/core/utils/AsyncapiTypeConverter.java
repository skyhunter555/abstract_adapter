package ru.syntez.adapter.core.utils;

import ru.syntez.adapter.core.entities.asyncapi.AsyncapiFormatEnum;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiTypeEnum;
/**
 * Преобразует тип данных из настроек asyncapi в конкретные классы
 * В asyncapi кроме типа данных есть формат данных. Их сочетание может дать новый класс.
 *
 * @author Skyhunter
 * @date 19.01.2022
 */
public class AsyncapiTypeConverter {

    /**
     * Получение класса из типа - упрощенный вариант
     * @return result with Class
     */
    public static Class<?> getPropertyClassFromType(String propertyTypeCode) {
        return getPropertyClassFromType(AsyncapiTypeEnum.forValues(propertyTypeCode));
    }

    /**
     * Получение класса из типа - упрощенный вариант
     * @return result with Class
     */
    public static Class<?> getPropertyClassFromType(AsyncapiTypeEnum propertyType) {
        if (propertyType == null) {
            return Object.class;
        }
        switch (propertyType) {
            case BOOLEAN:
                return Boolean.class;
            case INTEGER:
                return Integer.class;
            case NUMBER:
                return Double.class;
            case STRING:
                return String.class;
        }
        return Object.class;
    }

    /**
     * Получение класса из формата
     * @return result with Class
     */
    public static Class<?> getPropertyClassFromFormat(String propertyFormatCode) {
        return getPropertyClassFromFormat(AsyncapiFormatEnum.forValues(propertyFormatCode));
    }

    /**
     *     INT32   (AsyncapiTypeEnum.INTEGER,"int32"),
     *     INT64   (AsyncapiTypeEnum.INTEGER,"int64"),
     *     FLOAT   (AsyncapiTypeEnum.NUMBER, "float"),
     *     DOUBLE  (AsyncapiTypeEnum.NUMBER, "double"),
     *     BYTE    (AsyncapiTypeEnum.STRING, "byte"),
     *     BINARY  (AsyncapiTypeEnum.STRING, "binary"),
     *     DATE    (AsyncapiTypeEnum.STRING, "date"),
     *     DATETIME(AsyncapiTypeEnum.STRING, "date-time"),
     *     PASSWORD(AsyncapiTypeEnum.STRING, "password"),
     *     EMAIL   (AsyncapiTypeEnum.STRING, "email");
     * @param propertyFormat
     * @return
     */
    public static Class<?> getPropertyClassFromFormat(AsyncapiFormatEnum propertyFormat) {
        if (propertyFormat == null) {
            return Object.class;
        }
        switch (propertyFormat) {
            case INT32:
                return Integer.class;
            case INT64:
                return Long.class;
            case FLOAT:
                return Float.class;
            case DOUBLE:
                return Double.class;
            case BYTE:
                return Byte.class;
            case BINARY:
            case DATE:
            case DATETIME:
            case PASSWORD:
            case EMAIL:
                return String.class;
        }
        return Object.class;
    }
}
