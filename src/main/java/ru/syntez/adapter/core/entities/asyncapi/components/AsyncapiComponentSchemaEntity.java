package ru.syntez.adapter.core.entities.asyncapi.components;

import lombok.Data;

import java.io.Serializable;

/**
 * Asyncapi components
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
public class AsyncapiComponentSchemaEntity implements Serializable {

    private String type;
    private AsyncapiComponentSchemaProperties properties;

}
