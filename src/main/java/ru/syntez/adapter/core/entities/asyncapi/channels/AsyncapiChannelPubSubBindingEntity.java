package ru.syntez.adapter.core.entities.asyncapi.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiReference;

import java.io.Serializable;
import java.util.List;

/**
 * Asyncapi ChannelEntity Publish/Subscribe
 *
 topic: demoInput
 partitionCount: '10'
 servers: [
 $ref: '#/servers/dataproviders'
 ]
 *
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiChannelPubSubBindingEntity implements Serializable {
    private String topic;
    private String partitionCount;
    private List<AsyncapiReference> servers;
}
