package ru.syntez.adapter.core.usecases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.core.entities.HandleMessageResult;
import ru.syntez.adapter.core.components.IAdapterConverter;
import ru.syntez.adapter.core.entities.IMessageOutput;
import ru.syntez.adapter.core.entities.IMessagePayload;
import ru.syntez.adapter.core.entities.IMessageReceived;
import ru.syntez.adapter.core.entities.TransformResult;
import ru.syntez.adapter.core.exceptions.AdapterException;

import java.util.ArrayList;
import java.util.List;

/**
 * Выполнение трансформации сообщений с помощью MapStruct
 * На данный момент реализована простая трансформация один к одному.
 * Предполагается что более сложная трансформация должна быть реализована в BusinessUsecase
 *
 * @author Skyhunter
 * @date 27.12.2021
 */
@Service
public class TransformUsecase {

    private static Logger LOG = LogManager.getLogger(TransformUsecase.class);

    private final IAdapterConverter converter;

    public TransformUsecase(IAdapterConverter converter) {
        this.converter = converter;
    }

    public TransformResult execute(IMessagePayload messageReceived) {

        TransformResult result = new TransformResult();
        List<IMessageOutput> messageList = new ArrayList<>();

        try {
            messageList.add(converter.convert(messageReceived));
        } catch (AdapterException ex) {
            LOG.error(ex.getMessage());
            result.setResult(HandleMessageResult.ERROR);
        } catch (Exception ex) {
            LOG.error(String.format("Unknown error transform message: %s", ex.getMessage()));
            result.setResult(HandleMessageResult.ERROR);
        }

        result.setMessageOutputList(messageList);
        return result;
    }

}
