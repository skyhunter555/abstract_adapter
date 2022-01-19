package ru.syntez.adapter.core.entities.asyncapi.components;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * https://www.asyncapi.com/docs/specifications/v2.3.0-2022-01-release.2#correlationIdObject
 *
 * description: Default Correlation ID
 * location: $message.header#/correlationId
 *
 * Asyncapi component messages
 *
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiComponentMessageCorrelationID implements Serializable {
    String description;
    String location;
}
