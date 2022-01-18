package ru.syntez.adapter.core.utils;

import lombok.RequiredArgsConstructor;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiEntity;
import ru.syntez.adapter.core.entities.asyncapi.ServerProtocolEnum;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;

import java.util.Optional;

@RequiredArgsConstructor
public class AsyncapiService {

    private final AsyncapiEntity asyncapi;

    /**
     * @return result with Integer
     */
    public Optional<Integer> getEntrypointsHttpPort() {
        if (asyncapi.getServers() != null
                && asyncapi.getServers().getEntrypoints() != null
                && asyncapi.getServers().getEntrypoints().getProtocol() == ServerProtocolEnum.http) {
            return Optional.of(asyncapi.getServers().getEntrypoints().getVariables().getPort());
        }
        return Optional.empty();
    }

    public Optional<AsyncapiServerEntity> getEntrypointServer() {
        if (asyncapi.getServers() != null
                && asyncapi.getServers().getEntrypoints() != null) {
            return Optional.of(asyncapi.getServers().getEntrypoints());
        }
        return Optional.empty();
    }

    public Optional<AsyncapiServerEntity> getDataproviderServer() {
        if (asyncapi.getServers() != null
                && asyncapi.getServers().getDataproviders() != null) {
            return Optional.of(asyncapi.getServers().getDataproviders());
        }
        return Optional.empty();
    }


}
