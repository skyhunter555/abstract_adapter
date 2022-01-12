package ru.syntez.adapter.core.entities.asyncapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Asyncapi oneOf References
 * $ref: '#/servers/dataproviders'
 *
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiOneOfReference {

    @JsonProperty("$ref")
    private String reference;

    private List<AsyncapiReference> oneOf;

}
