package ru.syntez.adapter.core.entities.asyncapi.components.transform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 *  @author Skyhunter
 *  @date 25.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiSchemaTransform implements Serializable {

    private List<AsyncapiSchemaTransformSourceField> sourceFields;
    private Class<?> resultFieldClass;   // String.class
    private String resultFieldPattern;  // Платёжное поручение {0} от {1}

}
