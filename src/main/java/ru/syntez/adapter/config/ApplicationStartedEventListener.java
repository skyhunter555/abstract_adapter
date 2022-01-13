package ru.syntez.adapter.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiEntity;

@Slf4j
public class ApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent>, ApplicationContextAware {

    private final AsyncapiEntity asyncapi;

    public ApplicationStartedEventListener(AsyncapiEntity asyncapi) {
        this.asyncapi =  asyncapi;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {

        //TODO generate

        System.out.println(event.toString());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
