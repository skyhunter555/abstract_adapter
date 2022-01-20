package ru.syntez.adapter.core.usecases.generate;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.config.IDataproviderKafkaConfig;
import ru.syntez.adapter.core.components.IDataprovider;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiProtocolEnum;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentMessageEntity;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.core.utils.AsyncapiService;
import ru.syntez.adapter.dataproviders.kafka.DynamicKafkaConfigGenerator;
import ru.syntez.adapter.dataproviders.kafka.DynamicKafkaProducerGenerator;

import java.util.Optional;

/**
 * В зависимости от полученной конфигурации asyncapi
 * генерация компонентов контроллера исходящих сообщений:
 * kafka
 *
 * @author Skyhunter
 * @date 17.01.2022
 */
@Service
@RequiredArgsConstructor
public class GenerateDataproviderUsecase {

    private final GenerateMessageClassUsecase generateMessageClass;
    private final DynamicKafkaConfigGenerator kafkaConfigGenerator;
    private final DynamicKafkaProducerGenerator kafkaProducerGenerator;

    private static Logger LOG = LogManager.getLogger(GenerateDataproviderUsecase.class);

    public void execute(AsyncapiServerEntity dataproviderServer) {

        if (dataproviderServer.getProtocol() == null) {
            throw new AsyncapiParserException("Asyncapi dataprovider protocol not found!");
        }

        if (dataproviderServer.getProtocol() == AsyncapiProtocolEnum.kafka) {

            IDataproviderKafkaConfig kafkaConfig = kafkaConfigGenerator.execute(dataproviderServer);
            IDataprovider kafkaProducer = kafkaProducerGenerator.execute();

            LOG.info("Asyncapi kafka producer generated");
        }

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
