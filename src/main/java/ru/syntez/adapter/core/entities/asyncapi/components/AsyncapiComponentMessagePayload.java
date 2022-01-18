package ru.syntez.adapter.core.entities.asyncapi.components;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiReference;

import java.io.Serializable;

/**
 * Asyncapi component message payload
 *
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiComponentMessagePayload implements Serializable {
    private String type;                // string
    private String value;               // "OK"

    @JsonProperty("$ref")
    private String reference;  // '#/components/schemas/messageReceivedPayload'
}
