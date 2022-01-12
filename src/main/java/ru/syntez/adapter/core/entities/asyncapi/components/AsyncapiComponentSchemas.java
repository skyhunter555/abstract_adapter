package ru.syntez.adapter.core.entities.asyncapi.components;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Asyncapi components
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiComponentSchemas {
    private AsyncapiComponentSchemaEntity messageReceivedPayload;
    private AsyncapiComponentSchemaEntity messageOutputPayload;
    private AsyncapiComponentSchemaEntity sentAt;
}