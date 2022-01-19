package ru.syntez.adapter.core.entities.asyncapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * https://www.asyncapi.com/docs/specifications/v2.3.0-2022-01-release.2#externalDocumentationObject
 * Asyncapi External Documentation Object
 *
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiExternalDoc implements Serializable {
    private String description;
    private String url;
}
