package ru.syntez.adapter.core.entities.asyncapi.servers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Asyncapi Servers
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiServers implements Serializable {

    private AsyncapiServerEntity entrypoints;    // сервер входящих сообщений
    private AsyncapiServerEntity dataproviders;  // сервер исходящих сообщений

}
