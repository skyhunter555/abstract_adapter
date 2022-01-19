package ru.syntez.adapter.core.entities.asyncapi.components;

import lombok.Data;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiFormatEnum;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiTypeEnum;

import java.io.Serializable;
import java.util.List;

/**
 * https://www.asyncapi.com/docs/specifications/v2.3.0-2022-01-release.2#schemaObject
 *
 * type: object
 * properties:
 *   id:
 *     type: integer
 *     format: int64
 *   name:
 *     type: string
 * required:
 * - name
 * example:
 *   name: Puma
 *   id: 1
 *
 *
 *
 * Asyncapi components
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
public class AsyncapiComponentSchemaEntity implements Serializable {

    private AsyncapiTypeEnum type;
    private AsyncapiFormatEnum format;  // enum
    private String title;
    private List<String> required;
    private AsyncapiComponentSchemaProperties properties;

}
