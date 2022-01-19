package ru.syntez.adapter.core.entities.asyncapi.components;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Asyncapi components
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiComponentSchemas implements Serializable {
    private AsyncapiComponentSchemaEntity messageReceivedPayload;
    private AsyncapiComponentSchemaEntity messageOutputPayload;
    private AsyncapiComponentSchemaEntity sentAt;
    private AsyncapiComponentSchemaEntity messageTranformPayload;
}
