package ru.syntez.adapter.core.components;

import ru.syntez.adapter.core.entities.IMessageOutput;
import ru.syntez.adapter.core.entities.IMessageReceived;

/**
 * Абстрактный конвертер
 *
 * @author Skyhunter
 * @date 27.12.2021
 */
public interface IAdapterConverter {

    IMessageOutput convert(IMessageReceived messageReceived);

}
