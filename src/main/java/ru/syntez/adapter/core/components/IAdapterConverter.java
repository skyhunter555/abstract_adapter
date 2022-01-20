package ru.syntez.adapter.core.components;

import ru.syntez.adapter.core.entities.IMessagePayload;

/**
 * Абстрактный конвертер
 *
 * @author Skyhunter
 * @date 27.12.2021
 */
public interface IAdapterConverter {

    IMessagePayload convert(Class<?> outputMessageClass, IMessagePayload messageReceived);

}
