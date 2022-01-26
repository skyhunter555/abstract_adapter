package ru.syntez.adapter.config;

import ru.syntez.adapter.core.entities.asyncapi.components.transform.AsyncapiSchemaTransform;

import java.util.Map;

/**
 * Конфигурация для трансформации
 *
 * @author Skyhunter
 * @date 20.01.2022
 */
public interface ITransformConfig {

    Class<?> messageOutputClass();

    Map<String, AsyncapiSchemaTransform> transformSchema();

}
