package ru.syntez.adapter.core.usecases.generate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.utils.AsyncapiService;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * В зависимости от полученной конфигурации asyncapi
 * генерация компонентов адаптера транспортного уровня:
 * сообщения,
 * контроллеры входящих и сходящих сообщений,
 * трансформеры
 *
 * @author Skyhunter
 * @date 17.01.2022
 */
@Service
@RequiredArgsConstructor
public class GenerateMainUsecase {

    private final AsyncapiService asyncapiService;
    private final GenerateEntrypointUsecase generateEntrypoint;
    private final GenerateDataproviderUsecase generateDataprovider;
    private final GenerateTransformUsecase generateTransform;

    @PostConstruct
    public void execute() {

        Optional<AsyncapiServerEntity> entrypointServer = asyncapiService.getEntrypointServer();
        entrypointServer.ifPresent(generateEntrypoint::execute);

        Optional<AsyncapiServerEntity> dataproviderServer = asyncapiService.getDataproviderServer();
        dataproviderServer.ifPresent(generateDataprovider::execute);

        generateTransform.execute();
    }

}

