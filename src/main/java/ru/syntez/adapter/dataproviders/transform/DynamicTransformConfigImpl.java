package ru.syntez.adapter.dataproviders.transform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentSchemaEntity;
import ru.syntez.adapter.core.entities.asyncapi.components.transform.AsyncapiSchemaTransform;

import java.util.Map;

/**
 *
 */
@Slf4j
@Service
public class DynamicTransformConfigImpl {

    private Map<String, AsyncapiSchemaTransform> transformSchema;
    private Class<?> messageOutputClass;

    public Map<String, AsyncapiSchemaTransform> getTransformSchema() {
        return transformSchema;
    }

    public void setTransformSchema(Map<String, AsyncapiSchemaTransform> transformSchema) {
        this.transformSchema = transformSchema;
    }

    public Class<?> getMessageOutputClass() {
        return messageOutputClass;
    }

    public void setMessageOutputClass(Class<?> messageOutputClass) {
        this.messageOutputClass = messageOutputClass;
    }
}
