// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition // https://giters.com/springdoc/springdoc-openapi/issues/2102
public class ExamsApplication {

    public static void main(String[] args) {

        SpringApplication.run(ExamsApplication.class, args);
    }
}
