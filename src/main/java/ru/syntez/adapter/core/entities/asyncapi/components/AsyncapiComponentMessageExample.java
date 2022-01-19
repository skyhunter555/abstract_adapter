package ru.syntez.adapter.core.entities.asyncapi.components;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiExternalDoc;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiPayloadEntity;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiReference;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiTag;

import java.io.Serializable;
import java.util.List;

/**
 * https://www.asyncapi.com/docs/specifications/v2.3.0-2022-01-release.2#messageExampleObject
 *
 * name: SimpleSignup
 * summary: A simple UserSignup example message
 * headers:
 *   correlationId: my-correlation-id
 *   applicationInstanceId: myInstanceId
 * payload:
 *   user:
 *     someUserKey: someUserValue
 *   signup:
 *     someSignupKey: someSignupValue
 *
 *
 * Asyncapi example message
 *
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiComponentMessageExample implements Serializable {

    private AsyncapiPayloadEntity headers;
    private AsyncapiPayloadEntity payload;  // '#/components/schemas/messageReceivedPayload'
    private AsyncapiComponentMessageCorrelationID correlationId;
    private String name;                // SampleDocument
    private String summary;             // Входящий документ

}
