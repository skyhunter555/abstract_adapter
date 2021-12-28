package ru.syntez.adapter.core.components;

import ru.syntez.adapter.core.entities.HandleMessageResult;
import ru.syntez.adapter.core.entities.IMessageOutput;

/**
 * Абстрактный data provider для отправки сообщения
 * @author Skyhunter
 * @date 26.12.2021
 */
public interface IDataprovider {

    HandleMessageResult sendMessage(IMessageOutput messageOutput) throws Exception;

}
