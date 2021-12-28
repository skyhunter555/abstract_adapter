package ru.syntez.adapter.dataproviders.kafka.entities.transform;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.syntez.adapter.core.components.IAdapterConverter;
import ru.syntez.adapter.core.entities.IMessageOutput;
import ru.syntez.adapter.core.entities.IMessageReceived;
import ru.syntez.adapter.core.exceptions.AdapterException;
import ru.syntez.adapter.entrypoints.http.entities.SampleDocument;

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
public class MapStructConverter implements IAdapterConverter {

    private final IMapStructConverter converter;

    public MapStructConverter(IMapStructConverter converter) {
        this.converter = converter;
    }

    public IMessageOutput convert(IMessageReceived messageReceived) {
        if (messageReceived instanceof SampleDocument) {
            return converter.convert((SampleDocument) messageReceived);
        }
        throw new AdapterException("Unexpected class implements IMessageReceived");
    }

}
