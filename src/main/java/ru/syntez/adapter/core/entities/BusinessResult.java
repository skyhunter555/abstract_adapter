package ru.syntez.adapter.core.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Результат выполнения бизнес логики:
 *  массив исходящих сообщений,
 *  статус выполнения
 *
 * В зависимости от усложнения логики возможно дальнейшее расширение
 * @author Skyhunter
 * @date 26.12.2021
 */
@Data
public class BusinessResult {
    private List<IMessagePayload> messageOutputList = new ArrayList<>();
    private HandleMessageResult result =  HandleMessageResult.OK;
}
