package ru.syntez.adapter.core.utils;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.syntez.adapter.config.ITransformConfig;
import ru.syntez.adapter.core.components.IAdapterConverter;
import ru.syntez.adapter.core.entities.IMessagePayload;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Реализация конвертации сообщений, для конкретных классов.
 * Для конвертации достаточно обойтись интерфейсом IMapStructConverter,
 * но т.к. нам необходимо вынести конкретные классы из TransformUsecase,
 * создан этот посредник.
 *
 * @author Skyhunter
 * @date 27.12.2021
 */
@Component
@Primary
@RequiredArgsConstructor
public class AdapterConverterImpl implements IAdapterConverter {

    private final ApplicationContext applicationContext;

    public IMessagePayload convert(IMessagePayload messageReceived) {

        ITransformConfig transformConfig = (ITransformConfig) applicationContext.getBean("TransformConfig");

        Class<?> outputMessageClass = transformConfig.outputMessageClass();
        Map<String, Object> transformSchema = transformConfig.transformSchema();

        try {

            IMessagePayload outputMessage = (IMessagePayload) outputMessageClass.newInstance();

            for (Map.Entry<String, Object> entry: transformSchema.entrySet()) {
                String sourceMessageName = getPropertyByKey(entry.getValue(), "sourceMessageName");
                String sourceFieldName = getPropertyByKey(entry.getValue(), "sourceFieldName");
                String sourceFieldType = getPropertyByKey(entry.getValue(), "sourceFieldType");
                String propertyName = entry.getKey();

                Method getFieldMethod = messageReceived.getClass().getDeclaredMethod("get" + sourceFieldName.substring(0, 1).toUpperCase() + sourceFieldName.substring(1));
                Method setFieldMethod = outputMessage.getClass().getDeclaredMethod("set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1),
                        AsyncapiTypeConverter.getPropertyClassFromType(sourceFieldType));
                setFieldMethod.invoke(outputMessage, getFieldMethod.invoke(messageReceived));
            }

            return outputMessage;

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;

    }

    public IMessagePayload convert(List<IMessagePayload> messageReceived) {
        return null;
    }

    private String getPropertyByKey(Object propertyMap, String key) {
        if (propertyMap instanceof Map) {
            val propertyValue = ((Map) propertyMap).get(key);
            if (propertyValue != null) {
                return propertyValue.toString();
            }
        }
        throw new AsyncapiParserException(String.format("Asyncapi property not found by key %s!", key));
    }

}
