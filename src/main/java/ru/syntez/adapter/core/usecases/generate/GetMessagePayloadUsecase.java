package ru.syntez.adapter.core.usecases.generate;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiTypeEnum;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentMessageEntity;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentSchemaEntity;
import ru.syntez.adapter.core.entities.asyncapi.components.properties.AsyncapiMessageProperty;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.core.utils.AsyncapiService;
import ru.syntez.adapter.core.utils.AsyncapiTypeConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
/**
 * GetMessagePayloadUsecase
 *
 * Получение полей сообщения из описания schema payload
 *
 * @author Skyhunter
 * @date 21.01.2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GetMessagePayloadUsecase {

    private final AsyncapiService asyncapiService;

    @SneakyThrows
    public Map<String, AsyncapiMessageProperty> execute(AsyncapiComponentMessageEntity messageEntity) {

        if (messageEntity.getName() == null) {
            throw new AsyncapiParserException("Asyncapi message name not found!");
        }

        if (messageEntity.getPayload() == null || messageEntity.getPayload().getReference() == null) {
            throw new AsyncapiParserException("Asyncapi message payload reference not found!");
        }

        Optional<AsyncapiComponentSchemaEntity> payloadSchemaOptional = asyncapiService.getMessagePayload(messageEntity.getPayload().getReference());

        if (!payloadSchemaOptional.isPresent()) {
            throw new AsyncapiParserException(
                    String.format("Asyncapi message payload schema not found by reference: %s",
                            messageEntity.getPayload().getReference()));
        }

        AsyncapiComponentSchemaEntity messagePayloadSchema = payloadSchemaOptional.get();

        //Тип payloadSchema обязательное поле, по которому определяются дальнейшие настройки
        if (messagePayloadSchema.getType() == null) {
            throw new AsyncapiParserException("Asyncapi message payload schema type not found!");
        }

        //Если payloadSchema является объектом у него должны быть указаны поля
        if (messagePayloadSchema.getType() == AsyncapiTypeEnum.OBJECT &&
                (messagePayloadSchema.getProperties() == null || messagePayloadSchema.getProperties().getAdditionalProperties() == null)) {
            throw new AsyncapiParserException("Asyncapi message payload properties not found!");
        }

        val schemaPayloadMap = messagePayloadSchema.getProperties().getAdditionalProperties();

        val messagePayloadMap = new HashMap<String, AsyncapiMessageProperty>();
        for (Map.Entry<String, Object> entry : schemaPayloadMap.entrySet()) {
            String propertyName = entry.getKey();
            AsyncapiMessageProperty messageProperty = new AsyncapiMessageProperty();
            messageProperty.setPropertyClass(getPropertyClass(entry.getValue()));
            messageProperty.setDescription(getPropertyValueString(entry.getValue(), "description"));
            messageProperty.setMinimum(getPropertyValueInteger(entry.getValue(), "minimum"));
            messageProperty.setMaximum(getPropertyValueInteger(entry.getValue(), "maximum"));
            messageProperty.setFormat(getPropertyValueString(entry.getValue(), "format"));
            messagePayloadMap.put(entry.getKey(), messageProperty);
        }
        return messagePayloadMap;
    }

    /**
     * Получение строкового значения настройки по ключу из настроек payload
     * @param propertyMap
     * @param propertyKey
     * @return
     */
    private String getPropertyValueString(Object propertyMap, String propertyKey) {
        if (propertyMap instanceof Map) {
            Object propertyValue = ((Map) propertyMap).get(propertyKey);
            if (propertyValue != null) {
                return propertyValue.toString();
            }
        }
        return null;
    }

    /**
     * Получение строкового значения настройки по ключу из настроек payload
     * @param propertyMap
     * @param propertyKey
     * @return
     */
    private Integer getPropertyValueInteger(Object propertyMap, String propertyKey) {
        if (propertyMap instanceof Map) {
            Object propertyValue = ((Map) propertyMap).get(propertyKey);
            if (propertyValue != null) {
                return Integer.valueOf(propertyValue.toString());
            }
        }
        return null;
    }

    /**
     * Получение класса из настроек payload
     *
     * @param propertyMap
     * @return
     */
    private Class<?> getPropertyClass(Object propertyMap) {
        if (propertyMap instanceof Map) {
            val typeCode = ((Map) propertyMap).get("type");
            val formatCode = ((Map) propertyMap).get("format");
            if (formatCode != null) {
                return AsyncapiTypeConverter.getPropertyClassFromFormat(formatCode.toString());
            } else if (typeCode != null) {
                if (AsyncapiTypeEnum.OBJECT.getCode().equals(typeCode)) {
                    return getPropertyClassObject((Map) propertyMap);
                }
                return AsyncapiTypeConverter.getPropertyClassFromType(typeCode.toString());
            }
        }
        return Object.class;
    }

    /**
     * Если поле является объектом, то необходимо найти по ссылке этот объект и определить его класс
     *
     * @param propertyMap
     * @return
     */
    private Class<?> getPropertyClassObject(Map propertyMap) {

        Optional<AsyncapiComponentSchemaEntity> propertySchemaOptional = asyncapiService.getMessagePayload(propertyMap.get("$ref").toString());
        if (!propertySchemaOptional.isPresent()) {
            return Object.class;
        }

        AsyncapiComponentSchemaEntity propertySchema = propertySchemaOptional.get();

        if (propertySchema.getType() == null) {
            throw new AsyncapiParserException("Asyncapi message payload propertySchema type not found!");
        }
        if (propertySchema.getType() == AsyncapiTypeEnum.OBJECT &&
                (propertySchema.getProperties() == null || propertySchema.getProperties().getAdditionalProperties() == null)) {
            throw new AsyncapiParserException("Asyncapi message payload propertySchema properties not found!");
        }

        if (propertySchema.getFormat() != null) {
            return AsyncapiTypeConverter.getPropertyClassFromFormat(propertySchema.getFormat());
        }
        return AsyncapiTypeConverter.getPropertyClassFromType(propertySchema.getType());

    }

}
