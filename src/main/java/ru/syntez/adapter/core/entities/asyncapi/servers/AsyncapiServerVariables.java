package ru.syntez.adapter.core.entities.asyncapi.servers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Asyncapi server variables
 * @author Skyhunter
 * @date 13.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiServerVariables implements Serializable {

    private AsyncapiServerVariableEntity port;
    private AsyncapiServerVariableEntity basePath;

}
