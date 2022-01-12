package ru.syntez.adapter.core.entities.asyncapi.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiOneOfReference;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiReference;

import java.util.List;

/**
 * Asyncapi ChannelEntity Publish/Subscribe
 * @author Skyhunter
 * @date 12.01.2022
 *
 *       operationId: receiveSampleDocumentResponse
 *       bindings:
 *         http:
 *           servers: [
 *               $ref: '#/servers/entrypoints'
 *           ]
 *       summary: >
 *         Ответ по успешности обработки документа
 *       description: >
 *         Если документ успешно обработан то ответ OK, если успеха нет то ERROR.
 *       tags:
 *         - name: SampleDocument
 *       message:
 *         oneOf:
 *           - $ref: '#/components/messages/ResultOK'
 *           - $ref: '#/components/messages/ResultERROR'
 */
@Data
@JsonIgnoreProperties
public class AsyncapiChannelEntityPubSub {

    private String operationId;
    private String summary;
    private String description;
    private AsyncapiChannelPubSubBindings bindings;
    private List<AsyncapiChannelEntityPubSubTag> tags;
    private AsyncapiOneOfReference message;
}
