package ru.syntez.adapter.entrypoints.http;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Argument;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.syntez.adapter.core.entities.HandleMessageResult;
import ru.syntez.adapter.core.entities.IMessageReceived;
import ru.syntez.adapter.core.usecases.HandleMessageUsecase;

import java.lang.reflect.Modifier;

/**
 * Generates rest controller for {@link SampleDocument} at runtime:
 * {@code
 *
 * package ru.syntez.adapter.entrypoints.http;
 * import io.swagger.annotations.Api;
 * import io.swagger.annotations.ApiOperation;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.web.bind.annotation.*;
 * import ru.syntez.adapter.core.entities.HandleMessageResult;
 * import ru.syntez.adapter.core.usecases.HandleMessageUsecase;
 * import ru.syntez.adapter.entrypoints.http.entities.SampleDocument;
 *
 * @RestController
 * @RequestMapping("/sample-document/api/v1")
 * @Api(value = "sample-document")
 * public class AdapterController {
 *
 *     private final HandleMessageUsecase handleMessageUsecase;
 *
 *     public AdapterController(HandleMessageUsecase handleMessageUsecase) {
 *         this.handleMessageUsecase = handleMessageUsecase;
 *     }
 *
 *     @PostMapping
 *     @ApiOperation(value = "Handle sample document", produces = "application/json")
 *     public HandleMessageResult create(
 *             @RequestBody SampleDocument sampleDocument
 *     ) {
 *         return handleMessageUsecase.execute(sampleDocument);
 *     }
 *
 * }
 */
@Slf4j
@Component
@DependsOn("handleMessageUsecase")
@RequiredArgsConstructor
public class DynamicHttpControllerGenerator {

    private final ApplicationContext applicationContext;

    @SneakyThrows
    public Object generateHttpController() {
        // init static implementation to avoid reflection usage
        HttpControllerMethodsImplementation.handleMessageUsecase = applicationContext.getBean(HandleMessageUsecase.class);

        // creates builder with unique `class` name and `@RestController` annotation
        Object adapterHttpController = new ByteBuddy()
                .subclass(Object.class)
                .name("AdapterHttpController")
                .annotateType(AnnotationDescription.Builder
                        .ofType(RestController.class) // don't use `request` mapping here
                        .build())
                .annotateType(AnnotationDescription.Builder.ofType(Api.class)
                    .define("value", "sample-document")
                    .build())
                /**
                 * Delegates to:
                 * {@link HttpControllerMethodsImplementation#create(SampleDocument)}
                 */
                .defineMethod("create", HandleMessageResult.class, Modifier.PUBLIC)
                .withParameter(SampleDocument.class, "sampleDocument")
                .annotateParameter(AnnotationDescription.Builder
                        .ofType(RequestBody.class)
                        .build())
                .intercept(MethodDelegation.to(HttpControllerMethodsImplementation.class))

                // creates instance of generated `controller`
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();

        log.info("Generated `AdapterHttpController`: {}", adapterHttpController.getClass().getName());
        return adapterHttpController;
    }

    /**
     * Methods implementation for {@link SampleDocument} controller by {@link HandleMessageUsecase}
     */
    public static class HttpControllerMethodsImplementation {

        private static HandleMessageUsecase handleMessageUsecase;

        /**
         * Delegates to:
         * {@link HandleMessageUsecase#execute(IMessageReceived)}
         */
        public static HandleMessageResult create(@Argument(0) SampleDocument sampleDocument) {
            return handleMessageUsecase.execute(sampleDocument);
        }

    }

}
