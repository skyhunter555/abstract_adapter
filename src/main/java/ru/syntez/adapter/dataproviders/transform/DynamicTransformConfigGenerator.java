package ru.syntez.adapter.dataproviders.transform;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.MethodDelegation;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.syntez.adapter.config.ITransformConfig;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentSchemaEntity;
import ru.syntez.adapter.core.entities.asyncapi.components.transform.AsyncapiSchemaTransform;

import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Генератор настроек трансформации
 *
 * @author Skyhunter
 * @date 20.01.2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicTransformConfigGenerator {

    private final ApplicationContext applicationContext;
    private final DynamicTransformConfigImpl transformConfig;

    @SneakyThrows
    public ITransformConfig execute(Class<?> outputMessagePayloadClass, Map<String, AsyncapiSchemaTransform> transformSchema) {

        TransformConfigBeanImplementation.transformConfigImpl = transformConfig;

        //Задаем параметры конфигурации трансформера из настроек asyncapi
        transformConfig.setOutputMessageClass(outputMessagePayloadClass);
        transformConfig.setTransformSchema(transformSchema);

        ITransformConfig transformConfig = new ByteBuddy()
                .subclass(ITransformConfig.class)
                .name("TransformConfig")
                .annotateType(AnnotationDescription.Builder
                        .ofType(Configuration.class) // don't use `request` mapping here
                        .build())
                .annotateType(AnnotationDescription.Builder
                        .ofType(Primary.class) // don't use `request` mapping here
                        .build())
                .defineMethod("outputMessageClass", Class.class, Modifier.PUBLIC)
                .intercept(MethodDelegation.to(DynamicTransformConfigGenerator.TransformConfigBeanImplementation.class))
                .annotateMethod(AnnotationDescription.Builder
                        .ofType(Bean.class)
                        .build())
                .defineMethod("transformSchema", Map.class, Modifier.PUBLIC)
                .intercept(MethodDelegation.to(DynamicTransformConfigGenerator.TransformConfigBeanImplementation.class))
                .annotateMethod(AnnotationDescription.Builder
                        .ofType(Bean.class)
                        .build())

                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();

        //Регистрируем новые бины конфигурации
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        beanFactory.registerSingleton(transformConfig.getClass().getCanonicalName(), transformConfig);

        log.info("Generated `transformConfig`: {}", transformConfig.getClass().getName());
        return transformConfig;
    }

    /**
     * Methods implementation transformConfig by {@link DynamicTransformConfigImpl}
     */
    public static class TransformConfigBeanImplementation {

        private static DynamicTransformConfigImpl transformConfigImpl;

        /**
         * Delegates to:
         * {@link DynamicTransformConfigImpl#getOutputMessageClass()}
         */
        public static Class<?> outputMessageClass() {
            return transformConfigImpl.getOutputMessageClass();
        }

        public static Map<String, AsyncapiSchemaTransform> transformSchema() {
            return transformConfigImpl.getTransformSchema();
        }

    }
}
