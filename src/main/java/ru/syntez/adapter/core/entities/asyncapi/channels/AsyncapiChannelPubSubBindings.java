package ru.syntez.adapter.core.entities.asyncapi.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Asyncapi ChannelEntity Publish/Subscribe
 *
 *       bindings:
 *         http:
 *           servers: [
 *               $ref: '#/servers/entrypoints'
 *           ]
 *
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiChannelPubSubBindings {
    AsyncapiChannelPubSubBindingEntity http;
    AsyncapiChannelPubSubBindingEntity kafka;
    AsyncapiChannelPubSubBindingEntity rabbit;
}
