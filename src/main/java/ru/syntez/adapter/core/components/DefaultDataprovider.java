package ru.syntez.adapter.core.components;

import org.springframework.stereotype.Component;
import ru.syntez.adapter.core.entities.HandleMessageResult;
import ru.syntez.adapter.core.entities.IMessagePayload;

/**
 * Реализация дефалтного провайдера
 *
 * @author Skyhunter
 * @date 27.12.2021
 */
@Component
public class DefaultDataprovider implements IDataprovider {

    @Override
    public HandleMessageResult sendMessage(IMessagePayload messageOutput) {
        return HandleMessageResult.OK;
    }
}
