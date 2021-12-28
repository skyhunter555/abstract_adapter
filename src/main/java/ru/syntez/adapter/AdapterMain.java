package ru.syntez.adapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Main class for console running
 *
 * @author Skyhunter
 * @date 26.12.2021
 */
@SpringBootApplication
@EnableSwagger2
public class AdapterMain {

    public static void main(String[] args) {
        SpringApplication.run(AdapterMain.class, args);
    }

}