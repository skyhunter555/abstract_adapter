package ru.syntez.adapter.core.components;

import org.springframework.stereotype.Component;
import ru.syntez.adapter.core.entities.IMessagePayload;
import ru.syntez.adapter.core.exceptions.AdapterException;

/**
 * Реализация дефалтного конвертера
 *
 * @author Skyhunter
 * @date 27.12.2021
 */
@Component
public class DefaultConverter implements IAdapterConverter {

    public IMessagePayload convert(Class<?> outputMessageClass, IMessagePayload messageReceived) {
        throw new AdapterException("AdapterConverter not implemented!");
    }

}
