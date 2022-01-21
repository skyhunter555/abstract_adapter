package ru.syntez.adapter.core.usecases.generate;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentMessageEntity;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentSchemaEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.core.utils.AsyncapiService;
import java.util.Optional;
/**
 * GetMessagePayloadUsecase
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
    public AsyncapiComponentSchemaEntity execute(AsyncapiComponentMessageEntity messageEntity) {

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

        return payloadSchemaOptional.get();
    }

}
