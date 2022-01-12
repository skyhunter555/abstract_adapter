package ru.syntez.adapter.core.entities.asyncapi.components;

import lombok.Data;
/**
 * Asyncapi components
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
public class AsyncapiComponentSchemaEntity {

    private String type;
    private AsyncapiComponentSchemaProperties properties;

}
