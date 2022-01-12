package ru.syntez.adapter.core.entities.asyncapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponents;
import ru.syntez.adapter.core.entities.asyncapi.info.AsyncapiInfo;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServers;
import ru.syntez.adapter.core.entities.asyncapi.channels.AsyncapiChannels;
/**
 * Asyncapi entity
 * @author Skyhunter
 * @date 24.12.2021
 */
@Data
@JsonIgnoreProperties
public class AsyncapiEntity {

    private String asyncapi; //api version '2.0.0'
    private AsyncapiInfo info;
    private AsyncapiServers servers;
    private String defaultContentType; //application/json
    private AsyncapiChannels channels;
    private AsyncapiComponents components;

}
