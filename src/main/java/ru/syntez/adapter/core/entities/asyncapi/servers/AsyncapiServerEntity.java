package ru.syntez.adapter.core.entities.asyncapi.servers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.syntez.adapter.core.entities.asyncapi.ServerProtocolEnum;

/**
 * Asyncapi ServerEntity
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiServerEntity {

    private String url;             // 'localhost:9091'
    private String description;     // Кластер документов Kafka
    private ServerProtocolEnum protocol;        // kafka
    private String protocolVersion; //'2.5.1'

}
