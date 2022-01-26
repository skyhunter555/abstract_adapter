package ru.syntez.adapter.core.utils;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.syntez.adapter.config.ITransformConfig;
import ru.syntez.adapter.core.components.IAdapterConverter;
import ru.syntez.adapter.core.entities.IMessagePayload;
import ru.syntez.adapter.core.entities.asyncapi.components.transform.AsyncapiArithmeticPatternEnum;
import ru.syntez.adapter.core.entities.asyncapi.components.transform.AsyncapiSchemaTransform;
import ru.syntez.adapter.core.entities.asyncapi.components.transform.AsyncapiSchemaTransformSourceField;
import ru.syntez.adapter.core.exceptions.AdapterException;
import ru.syntez.adapter.core.usecases.generate.GenerateTransformUsecase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

    private static Logger LOG = LogManager.getLogger(GenerateTransformUsecase.class);

    private final ApplicationContext applicationContext;

    public IMessagePayload convert(IMessagePayload messageReceived) {

        val transformConfig = (ITransformConfig) applicationContext.getBean("TransformConfig");
        val messageOutputClass = transformConfig.messageOutputClass();
        val transformSchema = transformConfig.transformSchema();

        IMessagePayload messageOutput = null;
        try {
            messageOutput = (IMessagePayload) messageOutputClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, AsyncapiSchemaTransform> entry : transformSchema.entrySet()) {
            val messageReceivedList = new ArrayList<IMessagePayload>();
            messageReceivedList.add(messageReceived);
            setFieldValue(entry.getKey(), entry.getValue(), messageOutput, messageReceivedList);
        }

        LOG.error(String.format("AdapterConverter transform message successfully: %s", messageOutputClass.getName()));

        return messageOutput;

    }

    public IMessagePayload convert(List<IMessagePayload> messageReceived) {
        return null;
    }

    /**
     * Проставление значения полю, из полей входящих сообщений, в соответствии со схемой трансформации
     *
     * @param sourceFieldName
     * @param schemaTransform
     */
    private void setFieldValue(
            String sourceFieldName,
            AsyncapiSchemaTransform schemaTransform,
            IMessagePayload messageOutput,
            List<IMessagePayload> messageReceivedList
    ) {

        //Собираем значения из всех полей по списку
        val receivedValues = getReceivedValues(schemaTransform, messageReceivedList);

        //Заполнение строкового поля
        if (schemaTransform.getResultFieldClass() == String.class) {

            StringBuilder sourceFieldValue = new StringBuilder();
            //Реализация шаблона строки
            if (schemaTransform.getResultFieldPattern() != null) {
                sourceFieldValue = new StringBuilder(String.format(schemaTransform.getResultFieldPattern(), receivedValues.toArray()));
            } else {
                for (Object receivedValue : receivedValues) {
                    sourceFieldValue.append(receivedValue.toString());
                }
            }
            invokeSetFieldMethod(messageOutput, sourceFieldName, sourceFieldValue.toString());
        }

        //Заполнение целочисленного значения
        if (schemaTransform.getResultFieldClass() == Integer.class) {

            Integer sourceFieldValue = null;

            //суммируем значения всех полей
            if (schemaTransform.getResultFieldPattern() != null
                  && AsyncapiArithmeticPatternEnum.SUM.getCode().equals(schemaTransform.getResultFieldPattern())) {
                sourceFieldValue = 0;
                for (Object receivedValue : receivedValues) {
                    if (receivedValue instanceof Integer) {
                        sourceFieldValue = sourceFieldValue + (Integer) receivedValue;
                    } else if (receivedValue instanceof String) {
                        sourceFieldValue = sourceFieldValue + Integer.parseInt((String) receivedValue);
                    }
                }
            //В общем случае, если нет шаблона берем первое значение из списка
            //TODO сделать pattern count и т.д.
            } else {
                sourceFieldValue = (Integer) receivedValues.get(0);
            }
            invokeSetFieldMethod(messageOutput, sourceFieldName, sourceFieldValue);
        }
        //Заполнение численного значения
        if (schemaTransform.getResultFieldClass() == Double.class) {
            //TODO
        }
        //Заполнение логического значения
        if (schemaTransform.getResultFieldClass() == Boolean.class) {
            //TODO
        }

        //Заполнение объекта будет реализовано рекурсивно
        if (schemaTransform.getResultFieldClass() == Object.class) {
            //TODO
        }

    }

    /**
     * Собираем значения из всех полей по списку
     *
     * @param schemaTransform
     * @param messageReceivedList
     * @return
     */
    private List<Object> getReceivedValues(AsyncapiSchemaTransform schemaTransform, List<IMessagePayload> messageReceivedList) {
        val receivedValues = new ArrayList<>();
        for (AsyncapiSchemaTransformSourceField sourceField : schemaTransform.getSourceFields()) {
            String[] reference = sourceField.getSourceField().split("\\.");
            //Когда входящее сообщение одно - его класс можно не указывать (не рекомендуется)
            if (reference.length == 1) {
                if (messageReceivedList.size() > 1) {
                    throw new AdapterException("AdapterConverter invoke method error: messageClassName more than one");
                }
                receivedValues.add(
                        invokeGetFieldMethod(messageReceivedList.get(0), reference[0])
                );
                //Для плоских вариантов, без вложенных сущностей, когда сообщений более одного
            } else if (reference.length == 2) {
                String sourceFieldMessageClassName = reference[0];
                String sourceFieldName = reference[1];
                for (IMessagePayload messageReceived : messageReceivedList) {
                    if (messageReceived.getClass().getName().equals(sourceFieldMessageClassName)) {
                        receivedValues.add(
                                invokeGetFieldMethod(messageReceived, sourceFieldName)
                        );
                    }
                }
                //TODO реализовать кейс, когда есть вложенные классы
            } else if (reference.length > 2) {
                throw new AdapterException("AdapterConverter invoke method for Object not implemented.");
            }
        }
        return receivedValues;
    }

    /**
     * Вызов метода get из входящего сообщения
     *
     * @param messageReceived
     * @param sourceFieldName
     * @return
     */
    private Object invokeGetFieldMethod(IMessagePayload messageReceived, String sourceFieldName) {
        try {
            String getFieldMethodName = "get" + sourceFieldName.substring(0, 1).toUpperCase() + sourceFieldName.substring(1);
            Method getFieldMethod = messageReceived.getClass().getDeclaredMethod(getFieldMethodName);
            return getFieldMethod.invoke(messageReceived);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new AdapterException(String.format("AdapterConverter invoke method error: %s", e.getMessage()));
        }
    }

    private void invokeSetFieldMethod(IMessagePayload messageOutput, String sourceFieldName, Object sourceFieldValue) {
        try {
            String setFieldMethodName = "set" + sourceFieldName.substring(0, 1).toUpperCase() + sourceFieldName.substring(1);
            Method setFieldMethod = messageOutput.getClass().getDeclaredMethod(setFieldMethodName, sourceFieldValue.getClass());
            setFieldMethod.invoke(messageOutput, sourceFieldValue);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new AdapterException(String.format("AdapterConverter invoke method error: %s", e.getMessage()));
        }
    }
}
