package ru.syntez.adapter.core.entities.asyncapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Asyncapi Tag Object
 *
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiTag implements Serializable {
    private String name;
    private String description;
    private AsyncapiExternalDoc externalDocs;
}
