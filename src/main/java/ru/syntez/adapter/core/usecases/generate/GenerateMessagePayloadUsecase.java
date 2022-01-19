package ru.syntez.adapter.core.usecases.generate;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.springframework.stereotype.Component;
import ru.syntez.adapter.core.entities.IMessagePayload;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiTypeEnum;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentMessageEntity;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentSchemaEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.core.utils.AsyncapiService;
import ru.syntez.adapter.core.utils.AsyncapiTypeConverter;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * Generates rest controller for {@link IMessagePayload} at runtime:
 * @Data
 * public class SampleDocument implements Serializable, IMessagePayload {
 *
 *     private int docId;
 *     private String docNote;
 *     private String sentAt;
 *
 * }
 *
 * @author Skyhunter
 * @date 18.01.2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateMessagePayloadUsecase {

    private final AsyncapiService asyncapiService;

    @SneakyThrows
    public IMessagePayload execute(AsyncapiComponentMessageEntity messageEntity) {

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
        AsyncapiComponentSchemaEntity payloadSchema = payloadSchemaOptional.get();

        if (payloadSchema.getType() == null) {
            throw new AsyncapiParserException("Asyncapi message payload schema type not found!");
        }
        if (payloadSchema.getType() == AsyncapiTypeEnum.OBJECT &&
                (payloadSchema.getProperties() == null || payloadSchema.getProperties().getAdditionalProperties() == null)) {
            throw new AsyncapiParserException("Asyncapi message payload properties not found!");
        }

        Map<String, Object> payloadMap = payloadSchema.getProperties().getAdditionalProperties();

        DynamicType.Builder<IMessagePayload> messagePayloadBuilder = new ByteBuddy()
                .subclass(IMessagePayload.class)
                .implement(Serializable.class)
                .name(messageEntity.getName())
                .annotateType(AnnotationDescription.Builder
                        .ofType(Data.class)
                        .build());

        for (Map.Entry<String, Object> entry: payloadMap.entrySet()) {
            Class<?> propertyClass = getPropertyClass(entry.getValue());
            String propertyName = entry.getKey();
            messagePayloadBuilder = messagePayloadBuilder.defineProperty(propertyName, propertyClass);
        }

        // creates instance of generated `IMessagePayload`
        IMessagePayload messagePayload = messagePayloadBuilder
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER_PERSISTENT)
                .getLoaded()
                .newInstance();

        log.info("Generated `IMessagePayload`: {}", messagePayload.getClass().getName());
        return messagePayload;
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
                return AsyncapiTypeConverter.getPropertyClassFromType(typeCode.toString());
            }
        }
        return Object.class;
    }


}
