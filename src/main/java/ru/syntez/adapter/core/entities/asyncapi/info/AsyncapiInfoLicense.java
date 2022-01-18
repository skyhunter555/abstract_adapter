package ru.syntez.adapter.core.entities.asyncapi.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Asyncapi License
 * @author Skyhunter
 * @date 12.01.2022
 */
@Data
@JsonIgnoreProperties
public class AsyncapiInfoLicense implements Serializable {
    private String name; //Apache 2.0
    private String url;  //'https://www.apache.org/licenses/LICENSE-2.0'
}
