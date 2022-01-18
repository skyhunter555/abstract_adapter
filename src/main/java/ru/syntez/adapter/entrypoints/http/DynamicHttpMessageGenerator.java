package ru.syntez.adapter.entrypoints.http;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import org.springframework.stereotype.Component;
import ru.syntez.adapter.core.entities.IMessageReceived;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentMessageEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
/**
 * Generates rest controller for {@link SampleDocument} at runtime:
 * @Data
 * public class SampleDocument implements Serializable, IMessageReceived {
 *
 *     private int docId;
 *     private String docNote;
 *     private String sentAt;
 *
 * }
 * @author Skyhunter
 * @date 18.01.2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicHttpMessageGenerator {

    @SneakyThrows
    public IMessageReceived execute(AsyncapiComponentMessageEntity messageReceivedEntity) {

        if (messageReceivedEntity.getName() == null) {
            throw new AsyncapiParserException("Asyncapi entrypoint messageReceived name not found!");
        }

        IMessageReceived messageReceived = new ByteBuddy()
                .subclass(IMessageReceived.class)
                .name(messageReceivedEntity.getName())
                .annotateType(AnnotationDescription.Builder
                        .ofType(Data.class)
                        .build())

                // creates instance of generated `messageReceived`
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();

        log.info("Generated `messageReceived`: {}", messageReceived.getClass().getName());
        return messageReceived;
    }

}
