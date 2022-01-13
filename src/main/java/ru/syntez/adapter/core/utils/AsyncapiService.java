package ru.syntez.adapter.core.utils;

import ru.syntez.adapter.core.entities.asyncapi.AsyncapiEntity;
import ru.syntez.adapter.core.entities.asyncapi.ServerProtocolEnum;
import java.util.Optional;

public class AsyncapiService {

    /**
     * Parse String object value
     * @param asyncapi
     * @return result with Integer
     */
    public static Optional<Integer> getEntrypointsHttpPort(final AsyncapiEntity asyncapi) {
        if (asyncapi.getServers() != null
                && asyncapi.getServers().getEntrypoints() != null
                && asyncapi.getServers().getEntrypoints().getProtocol() == ServerProtocolEnum.http) {
            return Optional.of(asyncapi.getServers().getEntrypoints().getVariables().getPort());
        }
        return Optional.empty();
    }

}
