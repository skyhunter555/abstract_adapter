package ru.syntez.adapter.core.entities.asyncapi.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Asyncapi Info
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiInfo implements Serializable {

    private String title; // Demo sample document API
    private String version; //: '1.0.0'
    private String description; //Демонстрационный пример API для обработки документов, получаемых по http и отправки в Kafka.
    private AsyncapiInfoLicense license;

}
