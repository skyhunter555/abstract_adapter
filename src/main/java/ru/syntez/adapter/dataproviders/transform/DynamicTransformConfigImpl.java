package ru.syntez.adapter.dataproviders.transform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentSchemaEntity;

import java.util.Map;

/**
 *
 */
@Slf4j
@Service
public class DynamicTransformConfigImpl {

    private Map<String, Object> transformSchema;
    private Class<?> outputMessageClass;

    public Map<String, Object> getTransformSchema() {
        return transformSchema;
    }

    public void setTransformSchema(Map<String, Object> transformSchema) {
        this.transformSchema = transformSchema;
    }

    public Class<?> getOutputMessageClass() {
        return outputMessageClass;
    }

    public void setOutputMessageClass(Class<?> outputMessageClass) {
        this.outputMessageClass = outputMessageClass;
    }

}
