package ru.syntez.adapter.entrypoints.http;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;

/**
 * Registers rest controller for {@link SampleDocument}
 *
 * @see DynamicHttpControllerRegister
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicHttpControllerRegister {

    private final DynamicHttpControllerGenerator controllerGenerator;
    private final RequestMappingHandlerMapping handlerMapping;
    private final Docket api;

    @PostConstruct
    @SneakyThrows
    public void registerUserController() {
        Object adapterHttpController = controllerGenerator.generateHttpController();

        /**
         * Delegates to:
         * {@link AdapterHttpControllerMethodsImplementation#create(SampleDocument)}
         */
        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/sample-document/api/v1")
                        .methods(RequestMethod.POST)
                        .consumes(MediaType.APPLICATION_JSON_VALUE)
                        .build(),
                adapterHttpController,
                adapterHttpController.getClass()
                        .getMethod("create", SampleDocument.class));

        //Обновление swagger api
        api.select()
           .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
           .paths(PathSelectors.any()).build();

        log.info("Registered request handler for `AdapterHttpController`: {}", adapterHttpController.getClass().getName());
    }

}
