package ru.syntez.adapter.core.components;

import org.springframework.stereotype.Component;
import ru.syntez.adapter.core.entities.IMessageOutput;
import ru.syntez.adapter.core.entities.IMessagePayload;
import ru.syntez.adapter.core.entities.IMessageReceived;

/**
 * Реализация дефалтного конвертера
 *
 * @author Skyhunter
 * @date 27.12.2021
 */
@Component
public class DefaultConverter implements IAdapterConverter {

    public IMessageOutput convert(IMessagePayload messageReceived) {
        return null;
    }

}
