// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams;

import org.skypro.exams.model.question.Question;
import org.skypro.exams.model.storage.QuestionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
public class ExamsApplication {

    public static void main(String[] args) {

        QuestionRepository questionRepository = new QuestionRepository();
        questionRepository.addQuestion(new Question("Why?", "Just because"));
        questionRepository.addQuestion(new Question("Why so serious?", "I'm so happy about it"));

        try{
            //questionRepository.loadQuestionsFromTextFile("static/Questions-Java-Core.txt");

            questionRepository.loadQuestionsFromJson("static/Questions-Java-Core.json");
            questionRepository.saveQuestionsToJson("static/Questions-Java-Core.json");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        SpringApplication.run(ExamsApplication.class, args);
    }
}

