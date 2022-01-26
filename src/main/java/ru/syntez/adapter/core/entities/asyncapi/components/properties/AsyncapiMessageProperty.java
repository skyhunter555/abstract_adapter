package ru.syntez.adapter.core.entities.asyncapi.components.properties;

import lombok.Data;
import java.io.Serializable;
/**
 * Asyncapi components message properties from schema
 *
 * @author Skyhunter
 * @date 26.01.2022
 */
@Data
public class AsyncapiMessageProperty implements Serializable {

    private Class<?> propertyClass;
    private String description;
    private Integer minimum;
    private Integer maximum;
    private String format;

}
