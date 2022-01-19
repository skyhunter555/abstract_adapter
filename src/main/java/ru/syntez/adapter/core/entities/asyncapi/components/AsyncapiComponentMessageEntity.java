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
 * https://www.asyncapi.com/docs/specifications/v2.3.0-2022-01-release.2#messageObject
 *
 * name: UserSignup
 * title: User signup
 * summary: Action to sign a user up.
 * description: A longer description
 * contentType: application/json
 * tags:
 *   - name: user
 *   - name: signup
 *   - name: register
 * headers:
 *   type: object
 *   properties:
 *     correlationId:
 *       description: Correlation ID set by application
 *       type: string
 *     applicationInstanceId:
 *       description: Unique identifier for a given instance of the publishing application
 *       type: string
 * payload:
 *   type: object
 *   properties:
 *     user:
 *       $ref: "#/components/schemas/userCreate"
 *     signup:
 *       $ref: "#/components/schemas/signup"
 * correlationId:
 *   description: Default Correlation ID
 *   location: $message.header#/correlationId
 * traits:
 *   - $ref: "#/components/messageTraits/commonHeaders"
 * examples:
 *   - name: SimpleSignup
 *     summary: A simple UserSignup example message
 *     headers:
 *       correlationId: my-correlation-id
 *       applicationInstanceId: myInstanceId
 *     payload:
 *       user:
 *         someUserKey: someUserValue
 *       signup:
 *         someSignupKey: someSignupValue
 *
 * Asyncapi component message
 *
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiComponentMessageEntity implements Serializable {

    private AsyncapiPayloadEntity headers;
    private AsyncapiPayloadEntity payload;  // '#/components/schemas/messageReceivedPayload'
    private AsyncapiComponentMessageCorrelationID correlationId;

    //вызывает ошибку N[String(...)] is not a function
    //private String schemaFormat;      // 'application/vnd.apache.avro+yaml;version=1.9.0'

    private String contentType;         // application/json  text/html application/xhtml+xml application/xml  text/xml
    private String name;                // SampleDocument
    private String title;               // Входящий документ
    private String summary;             // Входящий документ
    private String description;
    private List<AsyncapiTag> tags;
    private AsyncapiExternalDoc externalDocs;
    private List<AsyncapiReference> bindings;
    private List<AsyncapiComponentMessageExample> examples;
    private AsyncapiReference traits;

}
