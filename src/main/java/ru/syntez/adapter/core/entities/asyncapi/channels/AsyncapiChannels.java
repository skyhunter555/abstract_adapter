package ru.syntez.adapter.core.entities.asyncapi.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Asyncapi channels
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiChannels implements Serializable {

    private AsyncapiChannelEntity receiveEvents;  // канал входящих сообщений
    private AsyncapiChannelEntity sendEvents;     // канал исходящих сообщений

}
