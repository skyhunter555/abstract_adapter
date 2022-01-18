package ru.syntez.adapter.core.usecases.generate;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.config.IKafkaConfig;
import ru.syntez.adapter.core.components.IDataprovider;
import ru.syntez.adapter.core.entities.asyncapi.ServerProtocolEnum;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.dataproviders.kafka.DynamicKafkaConfigGenerator;
import ru.syntez.adapter.dataproviders.kafka.DynamicKafkaProducerGenerator;

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

    private final DynamicKafkaConfigGenerator kafkaConfigGenerator;
    private final DynamicKafkaProducerGenerator kafkaProducerGenerator;

    private static Logger LOG = LogManager.getLogger(GenerateDataproviderUsecase.class);

    public void execute(AsyncapiServerEntity dataproviderServer) {

        if (dataproviderServer.getProtocol() == null) {
            throw new AsyncapiParserException("Asyncapi dataprovider protocol not found!");
        }

        if (dataproviderServer.getProtocol() == ServerProtocolEnum.kafka) {

            IKafkaConfig kafkaConfig = kafkaConfigGenerator.execute(dataproviderServer);
            IDataprovider kafkaProducer = kafkaProducerGenerator.execute();

            LOG.info("Asyncapi kafka producer generated");
        }

    }
}
