package ru.syntez.adapter.core.entities.asyncapi.components;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiReference;

/**
 * Asyncapi component message
 *
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiComponentMessageEntity {
    private String name;                // SampleDocument
    private String title;               // Входящий документ
    private String contentType;         // application/json
    private String summary;             // Входящий документ
    private AsyncapiComponentMessagePayload payload;  // '#/components/schemas/messageReceivedPayload'
}
