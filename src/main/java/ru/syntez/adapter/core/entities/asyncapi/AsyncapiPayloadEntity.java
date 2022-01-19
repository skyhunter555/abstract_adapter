package ru.syntez.adapter.core.entities.asyncapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Asyncapi payload
 *
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiPayloadEntity implements Serializable {
    private AsyncapiTypeEnum type;      // enum
    private AsyncapiFormatEnum format;  // enum
    private String value;               // "OK"

    @JsonProperty("$ref")
    private String reference;  // '#/components/schemas/messageReceivedPayload'

    private AsyncapiPayloadEntity transform;
}
