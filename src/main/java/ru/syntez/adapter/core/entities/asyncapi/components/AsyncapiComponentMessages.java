package ru.syntez.adapter.core.entities.asyncapi.components;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Asyncapi component messages
 *
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiComponentMessages implements Serializable {
    AsyncapiComponentMessageEntity messageReceived;
    AsyncapiComponentMessageEntity resultOK;
    AsyncapiComponentMessageEntity resultERROR;
    AsyncapiComponentMessageEntity messageOutput;
}
