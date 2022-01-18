package ru.syntez.adapter.core.usecases;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.core.entities.HandleMessageResult;
import ru.syntez.adapter.core.components.IDataprovider;
import ru.syntez.adapter.core.entities.IMessageOutput;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendMessageUsecase {

    private static Logger LOG = LogManager.getLogger(SendMessageUsecase.class);

    private final ApplicationContext applicationContext;

    public HandleMessageResult execute(List<IMessageOutput> messageOutputList) {

        IDataprovider producer = (IDataprovider) applicationContext.getBean("DataproviderImpl");

        try {
            for (IMessageOutput message: messageOutputList) {

                HandleMessageResult result = producer.sendMessage(message);
                //TODO логика зависит от требований
                if (result == HandleMessageResult.ERROR) {
                    return HandleMessageResult.ERROR;
                }
            }
        } catch (Exception ex) {
            LOG.error(String.format("Unknown error send message: %s", ex.getMessage()));
            return HandleMessageResult.ERROR;
        }

        return HandleMessageResult.OK;
    }

}
