package ru.syntez.adapter.core.utils;

import lombok.RequiredArgsConstructor;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiEntity;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

/**
 * Сервис сериализации файла asyncapi для добавления в системные настройки перед запуском, что-бы создать из него бин в конфигурации.
 * TODO подумать как обойтись без сериализации
 *
 * @author Skyhunter
 * @date 17.01.2022
 */
public class AsyncapiSerializer {

    public static String entityToString(AsyncapiEntity entity) {
        try {
            return toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AsyncapiEntity entityFromString(String asyncapiString) {
        try {
            return (AsyncapiEntity) fromString(asyncapiString);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Write the object to a Base64 string. */
    private static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    /** Read the object from Base64 string. */
    private static Object fromString( String s ) throws IOException, ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

}
