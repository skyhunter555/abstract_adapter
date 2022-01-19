package ru.syntez.adapter.core.usecases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.core.entities.BusinessResult;
import ru.syntez.adapter.core.entities.HandleMessageResult;
import ru.syntez.adapter.core.entities.IMessageOutput;
import ru.syntez.adapter.core.entities.IMessagePayload;
import ru.syntez.adapter.core.entities.IMessageReceived;
import ru.syntez.adapter.core.entities.TransformResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Обработка принятого сообщения:
 * какая-то бизнес логика или стандартная конвертация в формат исходящего сообщения (по дефалту json string)
 * отправка через data provider
 *
 * @author Skyhunter
 * @date 26.12.2021
 */
@Service
public class HandleMessageUsecase {

    private static Logger LOG = LogManager.getLogger(HandleMessageUsecase.class);

    private final BusinessUsecase businessUsecase;
    private final TransformUsecase transformUsecase;
    private final SendMessageUsecase sendMessageUsecase;

    @Value("${handle.execute-business}")
    private Boolean business;

    @Value("${handle.execute-transform}")
    private Boolean transform;

    public HandleMessageUsecase(BusinessUsecase businessUsecase, TransformUsecase transformUsecase, SendMessageUsecase sendMessageUsecase) {
        this.businessUsecase = businessUsecase;
        this.transformUsecase = transformUsecase;
        this.sendMessageUsecase = sendMessageUsecase;
    }

    public HandleMessageResult execute(IMessagePayload iMessagePayload) {

        List<IMessageOutput> messageOutputList = getMessageOutputList(iMessagePayload);
        try {
            return sendMessageUsecase.execute(messageOutputList);
        } catch (Exception ex) {
            LOG.error(String.format("Unknown error handle message: %s", ex.getMessage()));
            return HandleMessageResult.ERROR;
        }
    }

    /**
     * Получение исходящих сообщений для отправки
     *
     * @param messageReceived - входящее сообщение
     * @return
     */
    private List<IMessageOutput> getMessageOutputList(IMessagePayload messageReceived) {
        if (business) {
            BusinessResult result = businessUsecase.execute(messageReceived);
            if (result.getResult() == HandleMessageResult.OK) {
                return result.getMessageOutputList();
            }
        } else if (transform) {
            TransformResult result = transformUsecase.execute(messageReceived);
            if (result.getResult() == HandleMessageResult.OK) {
                return result.getMessageOutputList();
            }
        }

        // Если у нас не включена никакая бизнес логика и трансформация
        // или произошла какая то ошибка,
        // то никаких сообщений отправлено не будет
        // TODO уточнить требования

        return new ArrayList<>();
    }
}
