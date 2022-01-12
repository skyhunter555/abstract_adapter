package ru.syntez.adapter.core.entities.asyncapi.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Asyncapi ChannelEntity
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiChannelEntity {

    private AsyncapiChannelEntityPubSub subscribe;
    private AsyncapiChannelEntityPubSub publish;

}