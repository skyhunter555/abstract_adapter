package ru.syntez.adapter.core.usecases.generate;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.core.entities.IMessagePayload;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiProtocolEnum;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentMessageEntity;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.core.utils.AsyncapiService;
import ru.syntez.adapter.entrypoints.http.DynamicHttpControllerGenerator;
import ru.syntez.adapter.entrypoints.http.DynamicHttpControllerRegister;

import java.util.Optional;

/**
 * В зависимости от полученной конфигурации asyncapi
 * генерация компонентов контроллера входящих сообщений:
 * http
 *
 * @author Skyhunter
 * @date 17.01.2022
 */
@Service
@RequiredArgsConstructor
public class GenerateEntrypointUsecase {

    private final AsyncapiService asyncapiService;
    private final GenerateMessageClassUsecase generateMessageClass;
    private final DynamicHttpControllerGenerator controllerGenerator;
    private final DynamicHttpControllerRegister httpControllerRegister;

    private static Logger LOG = LogManager.getLogger(GenerateEntrypointUsecase.class);

    public void execute(AsyncapiServerEntity entrypointServer) {

        if (entrypointServer.getProtocol() == null) {
            throw new AsyncapiParserException("Asyncapi entrypoints protocol not found!");
        }

        if (entrypointServer.getProtocol() == AsyncapiProtocolEnum.http) {

            Class<?> messagePayloadClass = generateMessageClass.execute(getMessageReceived(asyncapiService));
            Object adapterHttpController = controllerGenerator.execute(messagePayloadClass);
            httpControllerRegister.execute(entrypointServer, adapterHttpController, messagePayloadClass);

            LOG.info("Asyncapi httpControllerRegister generated");
        }

    }

    /**
     * Получение настроек входящего сообщения
     * @param asyncapiService
     * @return
     */
    private AsyncapiComponentMessageEntity getMessageReceived(AsyncapiService asyncapiService) {

        Optional<AsyncapiComponentMessageEntity> messageReceived = asyncapiService.getMessageReceived();
        if (messageReceived.isPresent()) {
            return messageReceived.get();
        }
        throw new AsyncapiParserException("Asyncapi http entrypoint messageReceived not found!");
    }

}
