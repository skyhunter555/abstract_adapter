package ru.syntez.adapter.core.utils;

import lombok.RequiredArgsConstructor;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiEntity;
import ru.syntez.adapter.core.entities.asyncapi.ServerProtocolEnum;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentMessageEntity;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import java.util.Optional;

/**
 * Сервис для получения данных из настроек asyncapi
 * Создается при запуске приложения из файла asyncapi
 *
 * @author Skyhunter
 * @date 17.01.2022
 */
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

    /**
     * Получение настроек сервера входящих сообщений
     * @return
     */
    public Optional<AsyncapiServerEntity> getEntrypointServer() {
        if (asyncapi.getServers() != null
                && asyncapi.getServers().getEntrypoints() != null) {
            return Optional.of(asyncapi.getServers().getEntrypoints());
        }
        return Optional.empty();
    }

    /**
     * Получение настроек сервера исходящих сообщений
     * @return
     */
    public Optional<AsyncapiServerEntity> getDataproviderServer() {
        if (asyncapi.getServers() != null
                && asyncapi.getServers().getDataproviders() != null) {
            return Optional.of(asyncapi.getServers().getDataproviders());
        }
        return Optional.empty();
    }

    /**
     * Получение наименования топика для исходящих сообщений в kafka
     * @return
     */
    public Optional<String> getDataproviderKafkaTopicName() {
        if (asyncapi.getChannels() == null
                || asyncapi.getChannels().getSendEvents() == null
                || asyncapi.getChannels().getSendEvents().getPublish() == null
                || asyncapi.getChannels().getSendEvents().getPublish().getBindings() == null
                || asyncapi.getChannels().getSendEvents().getPublish().getBindings().getKafka() == null) {
            return Optional.empty();
        }
        return Optional.of(asyncapi.getChannels().getSendEvents().getPublish().getBindings().getKafka().getTopic());
    }

    /**
     * Получение сущности входящего сообщения
     * @return
     */
    public Optional<AsyncapiComponentMessageEntity> getMessageReceived() {
        if (asyncapi.getComponents() == null
                || asyncapi.getComponents().getMessages() == null
                || asyncapi.getComponents().getMessages().getMessageReceived() == null) {
            return Optional.empty();
        }
        return Optional.of(asyncapi.getComponents().getMessages().getMessageReceived());
    }

}
