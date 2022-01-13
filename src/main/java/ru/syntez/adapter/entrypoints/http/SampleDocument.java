package ru.syntez.adapter.entrypoints.http;

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

    private int docId;
    private String docNote;
    private String sentAt;

}
