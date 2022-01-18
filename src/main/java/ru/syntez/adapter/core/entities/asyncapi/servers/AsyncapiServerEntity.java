package ru.syntez.adapter.core.entities.asyncapi.servers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.syntez.adapter.core.entities.asyncapi.ServerProtocolEnum;

import java.io.Serializable;

/**
 * Asyncapi ServerEntity
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiServerEntity implements Serializable {

    private String url;                         // 'localhost:9091'
    private String description;                 // Кластер документов Kafka
    private ServerProtocolEnum protocol;        // kafka
    private String protocolVersion;             //'2.5.1'
    private AsyncapiServerVariables variables;  // port

}
