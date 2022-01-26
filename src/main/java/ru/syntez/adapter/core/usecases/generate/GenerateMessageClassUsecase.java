package ru.syntez.adapter.core.usecases.generate;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.springframework.stereotype.Component;
import ru.syntez.adapter.core.entities.IMessagePayload;
import ru.syntez.adapter.core.entities.asyncapi.components.properties.AsyncapiMessageProperty;
import java.io.Serializable;
import java.util.Map;

/**
 * Generates rest controller for {@link IMessagePayload} at runtime:
 *
 * @Data public class SampleDocument implements Serializable, IMessagePayload {
 * <p>
 * private int docId;
 * private String docNote;
 * private String sentAt;
 * <p>
 * }
 *
 * @author Skyhunter
 * @date 18.01.2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateMessageClassUsecase {

    @SneakyThrows
    public Class<?> execute(String messageClassName, Map<String, AsyncapiMessageProperty> messagePayloadMap) {

        DynamicType.Builder<IMessagePayload> messagePayloadBuilder = new ByteBuddy()
                .subclass(IMessagePayload.class)
                .implement(Serializable.class)
                .name(messageClassName)
                .annotateType(AnnotationDescription.Builder
                        .ofType(Data.class)
                        .build());

        for (Map.Entry<String, AsyncapiMessageProperty> messageProperty : messagePayloadMap.entrySet()) {
            messagePayloadBuilder = messagePayloadBuilder.defineProperty(
                    messageProperty.getKey(),
                    messageProperty.getValue().getPropertyClass()
            );
        }

        // creates instance of generated `IMessagePayload`
        Class<?> messagePayloadClass = messagePayloadBuilder
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();

        log.info("Generated implementation class for `IMessagePayload`: {}", messagePayloadClass.getName());
        return messagePayloadClass;
    }

}
