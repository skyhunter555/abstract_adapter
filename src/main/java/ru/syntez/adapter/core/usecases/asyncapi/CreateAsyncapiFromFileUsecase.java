package ru.syntez.adapter.core.usecases.asyncapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiEntity;
import ru.syntez.adapter.core.entities.asyncapi.FileExtensionEnum;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class CreateAsyncapiFromFileUsecase {

    private static Logger LOG = LogManager.getLogger(CreateAsyncapiFromFileUsecase.class);

    public AsyncapiEntity execute(String fileName) {

        File asyncapiFile = new File(fileName);
        if (!asyncapiFile.isFile()) {
            throw new AsyncapiParserException(String.format("This is not a file %s", fileName));
        }
        try {
            ObjectMapper objectMapper = getObjectMapper(fileName);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,  false);
            objectMapper.findAndRegisterModules();
            AsyncapiEntity asyncapi = objectMapper.readValue(asyncapiFile, AsyncapiEntity.class);

            LOG.info(String.format("File %s was completed", fileName));

            return asyncapi;
        } catch (IOException e) {
            throw new AsyncapiParserException(String.format("Error read file %s", fileName));
        }

    }

    private ObjectMapper getObjectMapper(String fileName) throws AsyncapiParserException {

        Optional<String> extensionOptional = getExtensionFile(fileName);

        if (!extensionOptional.isPresent()) {
            throw new AsyncapiParserException(String.format("Extension undefined for file %s", fileName));
        }

        FileExtensionEnum extension = FileExtensionEnum.parseCode(extensionOptional.get().toLowerCase());
        switch (extension) {
            case YML:
            case YAML:
                return new ObjectMapper(new YAMLFactory());
            case JSON:
                return new ObjectMapper();
        }
        throw new AsyncapiParserException(String.format("Parsing not yet implemented for file with extension %s", extension.getCode()));
    }

    private Optional<String> getExtensionFile(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

}
