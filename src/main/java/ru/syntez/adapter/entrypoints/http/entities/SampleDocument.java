package ru.syntez.adapter.entrypoints.http.entities;

import lombok.Data;
import ru.syntez.adapter.core.entities.IMessageReceived;

import java.io.Serializable;

/**
 * SampleDocument model
 *
 * @author Skyhunter
 * @date 26.12.2021
 */
@Data
public class SampleDocument implements Serializable, IMessageReceived {

    private int docId;      //Идентификатор документа.
    private String docNote; //Описание документа.
    private String sentAt;  //Время отправки сообщения в формате 'yyyy-MM-dd HH:mm:ss'.

}
