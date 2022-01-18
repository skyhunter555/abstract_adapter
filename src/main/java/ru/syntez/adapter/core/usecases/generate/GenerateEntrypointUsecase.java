package ru.syntez.adapter.core.usecases.generate;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.core.entities.asyncapi.ServerProtocolEnum;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.entrypoints.http.DynamicHttpControllerRegister;

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

    private final DynamicHttpControllerRegister httpControllerRegister;

    private static Logger LOG = LogManager.getLogger(GenerateEntrypointUsecase.class);

    public void execute(AsyncapiServerEntity entrypointServer) {

        if (entrypointServer.getProtocol() == null) {
            throw new AsyncapiParserException("Asyncapi entrypoints protocol not found!");
        }

        if (entrypointServer.getProtocol() == ServerProtocolEnum.http) {
            httpControllerRegister.execute(entrypointServer);
            LOG.info("Asyncapi httpControllerRegister generated");
        }

    }
}
