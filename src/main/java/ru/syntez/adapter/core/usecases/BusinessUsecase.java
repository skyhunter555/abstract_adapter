package ru.syntez.adapter.core.usecases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.core.entities.BusinessResult;
import ru.syntez.adapter.core.entities.IMessagePayload;
import ru.syntez.adapter.core.entities.IMessageReceived;

/**
 * Выполнение какой то бизнес логики, которая требует сложной трансформации сообщений
 * На данный момент не реализовано.
 * Предполагается что более простая трансформация должна быть реализована в TransformUsecase
 *
 * @author Skyhunter
 * @date 26.12.2021
 */
@Service
public class BusinessUsecase {

    private static Logger LOG = LogManager.getLogger(BusinessUsecase.class);

    public BusinessResult execute(IMessagePayload messageReceived) {

        //TODO какая-то бизнесовая логика

        return new BusinessResult();
    }

}
