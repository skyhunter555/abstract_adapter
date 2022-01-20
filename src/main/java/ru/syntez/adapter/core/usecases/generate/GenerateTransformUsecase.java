package ru.syntez.adapter.core.usecases.generate;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.config.IDataproviderKafkaConfig;
import ru.syntez.adapter.config.ITransformConfig;
import ru.syntez.adapter.core.components.IDataprovider;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiProtocolEnum;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentMessageEntity;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.core.utils.AsyncapiService;
import ru.syntez.adapter.dataproviders.kafka.DynamicKafkaConfigGenerator;
import ru.syntez.adapter.dataproviders.kafka.DynamicKafkaProducerGenerator;
import ru.syntez.adapter.dataproviders.transform.DynamicTransformConfigGenerator;

import java.util.Optional;

/**
 * В зависимости от полученной конфигурации asyncapi
 * генерация компонентов для простой трансформации сообщений с помощью конвертера
 * kafka
 *
 * @author Skyhunter
 * @date 20.01.2022
 */
@Service
@RequiredArgsConstructor
public class GenerateTransformUsecase {

    private final AsyncapiService asyncapiService;
    private final GenerateMessageClassUsecase generateMessageClass;
    private final DynamicTransformConfigGenerator transformConfigGenerator;

    private static Logger LOG = LogManager.getLogger(GenerateTransformUsecase.class);

    public void execute() {

        //if (dataproviderServer.getProtocol() == null) {
        //    throw new AsyncapiParserException("Asyncapi dataprovider protocol not found!");
        //}

        //if (dataproviderServer.getProtocol() == AsyncapiProtocolEnum.kafka) {

            Class<?> messagePayloadClass = generateMessageClass.execute(getMessageOutput(asyncapiService));
            ITransformConfig transformConfig = transformConfigGenerator.execute(messagePayloadClass);

            LOG.info("Asyncapi transtormer generated");
        //}

    }

    /**
     * Получение настроек входящего сообщения
     * @param asyncapiService
     * @return
     */
    private AsyncapiComponentMessageEntity getMessageOutput(AsyncapiService asyncapiService) {

        Optional<AsyncapiComponentMessageEntity> messageOutput = asyncapiService.getMessageOutput();
        if (messageOutput.isPresent()) {
            return messageOutput.get();
        }
        throw new AsyncapiParserException("Asyncapi kafka dataprovider messageOutput not found!");
    }
}
