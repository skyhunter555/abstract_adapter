package ru.syntez.adapter.core.utils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.syntez.adapter.core.components.IAdapterConverter;
import ru.syntez.adapter.core.entities.IMessagePayload;
import ru.syntez.adapter.core.exceptions.AdapterException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
public class AdapterConverterImpl implements IAdapterConverter {

    //private final IMapStructConverter converter;

    //public MapStructConverter(IMapStructConverter converter) {
    //    this.converter = converter;
    //}

    public IMessagePayload convert(Class<?> outputMessageClass, IMessagePayload messageReceived) {

        try {
            IMessagePayload outputMessage = (IMessagePayload) outputMessageClass.newInstance();

            Method getDocId = messageReceived.getClass().getDeclaredMethod("getDocId");
            Method getDocNote = messageReceived.getClass().getDeclaredMethod("getDocNote");

            Method setDocumentId = outputMessage.getClass().getDeclaredMethod("setDocumentId", Integer.class);
            Method setDocumentDescription = outputMessage.getClass().getDeclaredMethod("setDocumentDescription", String.class);

            setDocumentId.invoke(outputMessage, getDocId.invoke(messageReceived));
            setDocumentDescription.invoke(outputMessage, getDocNote.invoke(messageReceived));

            //LOG.info("Asyncapi kafka producer generated");

            return outputMessage;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;

    }

}
