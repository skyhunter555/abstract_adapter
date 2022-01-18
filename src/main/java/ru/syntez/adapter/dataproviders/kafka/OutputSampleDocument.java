package ru.syntez.adapter.dataproviders.kafka;

import lombok.Data;
import ru.syntez.adapter.core.entities.IMessageOutput;

import java.io.Serializable;

/**
 * OutputSampleDocument model
 *
 * @author Skyhunter
 * @date 26.12.2021
 */
@Data
public class OutputSampleDocument implements Serializable, IMessageOutput {

    private int documentId;
    private String documentDescription;

}