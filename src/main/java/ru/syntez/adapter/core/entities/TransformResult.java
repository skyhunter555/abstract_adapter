package ru.syntez.adapter.core.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Результат выполнения трансформации:
 *  массив исходящих сообщений,
 *  статус выполнения
 *
 * На начальном этапе похож на BusinessResult, но отличие в том, что тут особо нечего расширять
 * @author Skyhunter
 * @date 26.12.2021
 */
@Data
public class TransformResult {
    private List<IMessagePayload> messageOutputList = new ArrayList<>();
    private HandleMessageResult result =  HandleMessageResult.OK;
}
