package ru.syntez.adapter.core.usecases.generate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.config.ITransformConfig;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiTypeEnum;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentMessageEntity;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentSchemaEntity;
import ru.syntez.adapter.core.entities.asyncapi.components.properties.AsyncapiMessageProperty;
import ru.syntez.adapter.core.entities.asyncapi.components.transform.AsyncapiSchemaTransform;
import ru.syntez.adapter.core.entities.asyncapi.components.transform.AsyncapiSchemaTransformSourceField;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.core.utils.AsyncapiService;
import ru.syntez.adapter.dataproviders.transform.DynamicTransformConfigGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * В зависимости от полученной конфигурации asyncapi
 * генерация компонентов для простой трансформации сообщений с помощью конвертера
 * kafka
 *
 * @author Skyhunter
 * @date 20.01.2022
 */
@Service
@RequiredArgsConstructor
public class GenerateTransformUsecase {

    private final AsyncapiService asyncapiService;
    private final GenerateMessageClassUsecase generateMessageClass;
    private final DynamicTransformConfigGenerator transformConfigGenerator;
    private final GetMessagePayloadUsecase getMessagePayload;

    private static Logger LOG = LogManager.getLogger(GenerateTransformUsecase.class);

    public void execute() {

        Optional<AsyncapiComponentMessageEntity> messageOutputOptional = asyncapiService.getMessageOutput();

        //TODO Исходящего сообщения может и не быть, если включена какая то бизнес логика?
        if (!messageOutputOptional.isPresent()) {
            return;
        }

        AsyncapiComponentMessageEntity messageOutput = messageOutputOptional.get();

        //Получение payloadSchema исходящего сообщения, со списком полей и их типом
        val messagePayload = getMessagePayload.execute(messageOutput);

        Class<?> messagePayloadClass = generateMessageClass.execute(messageOutput.getName(), messagePayload);

        ITransformConfig transformConfig = transformConfigGenerator.execute(
                messagePayloadClass,
                getTransformSchema(messageOutput, messagePayload)
        );
        LOG.info("Asyncapi transtormer generated");

    }

    /**
     * Получение настроек трансформации для формирования исходящего сообщения
     *
     * @param messageOutput
     * @return
     */
    private Map<String, AsyncapiSchemaTransform> getTransformSchema(
            AsyncapiComponentMessageEntity messageOutput, //Исходящее сообщение
            Map<String, AsyncapiMessageProperty> messagePayload  //Список полей исходящего сообщения, для получения их типа
    ) {

        if (messageOutput.getPayload() == null
                || messageOutput.getPayload().getTransform() == null
                || messageOutput.getPayload().getTransform().getReference() == null) {

            //TODO Схемы трансформации сообщения может и не быть, если включена какая то бизнес логика?
            //В этом случае никакой трансформации не произойдет и исходящее сообщение останется пустым
            return new HashMap<>();

        }
        if (messagePayload == null) {
            //TODO Если исходящее сообщение не объект и у него нет списка полей это отдельный случай
            return new HashMap<>();
        }

        Optional<AsyncapiComponentSchemaEntity> transformSchemaOptional = asyncapiService.getMessagePayload(messageOutput.getPayload().getTransform().getReference());
        if (!transformSchemaOptional.isPresent()) {
            //Если нет ссылки на объект трансформации дальше работать не сможем
            throw new AsyncapiParserException("Asyncapi message transformSchema payload reference not found!");
        }

        AsyncapiComponentSchemaEntity transformSchema = transformSchemaOptional.get();

        if (transformSchema.getType() == null) {
            throw new AsyncapiParserException("Asyncapi message payload transformSchema type not found!");
        }
        if (transformSchema.getType() == AsyncapiTypeEnum.OBJECT &&
                (transformSchema.getProperties() == null || transformSchema.getProperties().getAdditionalProperties() == null)) {
            throw new AsyncapiParserException("Asyncapi message payload transformSchema properties not found!");
        }

        Map<String, AsyncapiSchemaTransform> schemaTransformMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : transformSchema.getProperties().getAdditionalProperties().entrySet()) {
            AsyncapiSchemaTransform schemaTransform = new AsyncapiSchemaTransform();

            //Получаем класс для поля сообщения и проставляем в схему трансформации
            AsyncapiMessageProperty messageProperty = messagePayload.get(entry.getKey());
            if (messageProperty != null && messageProperty.getPropertyClass() != null) {
                schemaTransform.setResultFieldClass(messageProperty.getPropertyClass());
            }

            schemaTransform.setResultFieldPattern(getStringPropertyByKey(entry.getValue(), "resultFieldPattern"));
            schemaTransform.setSourceFields(
                    getSourceFieldsByKey(entry.getValue(), "sourceFields")
            );
            schemaTransformMap.put(entry.getKey(), schemaTransform);
        }
        return schemaTransformMap;

    }

    private List<AsyncapiSchemaTransformSourceField> getSourceFieldsByKey(Object propertyMap, String key) {
        List<AsyncapiSchemaTransformSourceField> sourceFields = new ArrayList<>();
        if (propertyMap instanceof Map) {
            val propertyValueList = ((Map) propertyMap).get(key);
            if (propertyValueList != null) {
                if (propertyValueList instanceof ArrayList) {
                    for (Object propertyValueMap : (ArrayList) propertyValueList) {
                        if (propertyValueMap instanceof LinkedHashMap) {
                            AsyncapiSchemaTransformSourceField sourceField = new AsyncapiSchemaTransformSourceField();
                            sourceField.setSourceField(tryGetStringPropertyByKey(propertyValueMap, "sourceField"));
                            sourceFields.add(sourceField);
                        }
                    }
                }
            }
        }
        return sourceFields;
    }

    //Для обязательного значения
    private String tryGetStringPropertyByKey(Object propertyMap, String key) {
        String propertyValue = getStringPropertyByKey(propertyMap, key);
        if (propertyValue == null) {
            throw new AsyncapiParserException(String.format("Asyncapi property not found by key %s!", key));
        }
        return propertyValue;
    }

    //Для необязательного значения
    private String getStringPropertyByKey(Object propertyMap, String key) {
        if (propertyMap instanceof Map) {
            val propertyValue = ((Map) propertyMap).get(key);
            if (propertyValue != null) {
                return propertyValue.toString();
            }
        }
        return null;
    }
}
