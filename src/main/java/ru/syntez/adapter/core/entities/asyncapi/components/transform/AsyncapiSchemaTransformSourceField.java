package ru.syntez.adapter.core.entities.asyncapi.components.transform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 *  Описание исходного поля для получения результирующего поля
 *
 *  @author Skyhunter
 *  @date 25.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiSchemaTransformSourceField implements Serializable {

    private String sourceField;        // SampleDocument.sentAt
    private Class<?> sourceFieldType;  // Integer.class

}
