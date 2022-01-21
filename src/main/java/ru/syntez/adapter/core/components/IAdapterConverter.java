package ru.syntez.adapter.core.components;

import ru.syntez.adapter.core.entities.IMessagePayload;

import java.util.List;

/**
 * Абстрактный конвертер
 *
 * @author Skyhunter
 * @date 27.12.2021
 */
public interface IAdapterConverter {

    IMessagePayload convert(IMessagePayload messageReceived);

    IMessagePayload convert(List<IMessagePayload> messageReceived);

}
