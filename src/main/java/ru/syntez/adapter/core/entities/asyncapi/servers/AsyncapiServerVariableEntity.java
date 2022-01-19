package ru.syntez.adapter.core.entities.asyncapi.servers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * https://www.asyncapi.com/docs/specifications/v2.3.0-2022-01-release.2#serverVariableObject
 *
 *    variables:
 *       username:
 *         # note! no enum here means it is an open value
 *         default: demo
 *         description: This value is assigned by the service provider, in this example `gigantic-server.com`
 *       port:
 *         enum:
 *           - '8883'
 *           - '8884'
 *         default: '8883'
 *       basePath:
 *         # open meaning there is the opportunity to use special base paths as assigned by the provider, default is `v2`
 *         default: v2
 *
 * Asyncapi server variables
 * @author Skyhunter
 * @date 13.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiServerVariableEntity implements Serializable {

    @JsonProperty("enum")
    private List<String> enumList;

    @JsonProperty("default")
    private String defaultValue;

    private String description;

    private List<String> examples;

}
