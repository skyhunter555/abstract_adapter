package ru.syntez.adapter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiEntity;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.usecases.generate.GenerateDataproviderUsecase;
import ru.syntez.adapter.core.usecases.generate.GenerateEntrypointUsecase;
import ru.syntez.adapter.core.utils.AsyncapiSerializer;
import ru.syntez.adapter.core.utils.AsyncapiService;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationAsyncapiConfig {

    private final GenerateEntrypointUsecase generateEntrypoint;
    private final GenerateDataproviderUsecase generateDataprovider;
    private final AsyncapiService asyncapiService;

    @PostConstruct
    public void generator() {

        Optional<AsyncapiServerEntity> entrypointServer = asyncapiService.getEntrypointServer();
        entrypointServer.ifPresent(generateEntrypoint::execute);

        Optional<AsyncapiServerEntity> dataproviderServer = asyncapiService.getDataproviderServer();
        dataproviderServer.ifPresent(generateDataprovider::execute);

    }

}

