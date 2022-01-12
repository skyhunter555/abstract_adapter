package ru.syntez.adapter.usecases.asyncapi;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiEntity;
import ru.syntez.adapter.core.usecases.asyncapi.CreateAsyncapiFromFileUsecase;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CreateAsyncapiFromFileUsecaseTest {

    private static Logger LOG = LogManager.getLogger(CreateAsyncapiFromFileUsecase.class);

    @Test
    public void createFromYMLTest() {

        CreateAsyncapiFromFileUsecase createAsyncapiFromFileUsecase = new CreateAsyncapiFromFileUsecase();
        try {
            File file = ResourceUtils.getFile(this.getClass().getResource("/http-kafka.yml"));
            String fileName = file.getAbsolutePath();
            AsyncapiEntity asyncapi = createAsyncapiFromFileUsecase.execute(fileName);
            LOG.info("asyncapi: " + asyncapi);
        } catch (Exception e) {
            e.printStackTrace();
            fail( "error createAsyncapiFromFileUsecase" );
        }
    }

    @Test
    public void createDynamicInput() {

        CreateAsyncapiFromFileUsecase createAsyncapiFromFileUsecase = new CreateAsyncapiFromFileUsecase();
        try {

            DynamicType.Unloaded unloadedType = new ByteBuddy()
                    .subclass(Object.class)
                    .method(ElementMatchers.isToString())
                    .intercept(FixedValue.value("Hello World ByteBuddy!"))
                    .make();

            Class<?> dynamicType = unloadedType.load(getClass()
                    .getClassLoader())
                    .getLoaded();

            assertEquals(dynamicType.newInstance().toString(), "Hello World ByteBuddy!");
            //LOG.info("asyncapi: " + asyncapi);
        } catch (Exception e) {
            e.printStackTrace();
            fail( "error createAsyncapiFromFileUsecase" );
        }
    }

    @Test
    public void createFromJSONTest() {

        CreateAsyncapiFromFileUsecase createAsyncapiFromFileUsecase = new CreateAsyncapiFromFileUsecase();
        //TODO
    }

}
