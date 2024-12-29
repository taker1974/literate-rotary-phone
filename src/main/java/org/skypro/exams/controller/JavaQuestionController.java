// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.controller;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.service.java.JavaQuestionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Контроллер для работы с вопросами.<br>
 * Позволяет пользователю добавлять, просматривать и удалять вопросы по Java.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@Log4j2
@RestController
public class JavaQuestionController {

    @NotNull
    private final JavaQuestionService questionService;

    /**
     * Конструктор.
     *
     * @param questionService сервис для работы с вопросами
     */
    public JavaQuestionController(@NotNull final JavaQuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * Добавление вопроса.
     *
     * @param questionText текст вопроса
     * @param answerText   текст ответа
     */
    @PutMapping("/exam/java/add")
    public void addQuestion(final String questionText, final String answerText) {
        var question = questionService.addQuestion(questionText, answerText);
        log.info("Добавлен вопрос: {}", question);
    }

    /**
     * Получение всех вопросов.
     *
     * @return все вопросы
     */
    @RequestMapping("/exam/java")
    public Collection<Question> getQuestionsAll() {
        return questionService.getQuestionsAll();
    }

    /**
     * Удаление вопроса.
     *
     * @param questionText текст вопроса
     * @param answerText   текст ответа
     */
    @DeleteMapping("/exam/java/remove")
    public void removeQuestion(final String questionText, final String answerText) {
        var questionToRemove = new Question(questionText, answerText);
        questionService.getQuestionsAll().stream()
                .filter(question -> question.equals(questionToRemove))
                .findFirst()
                .ifPresent(o -> {
                    questionService.removeQuestion(o);
                    log.info("Удален вопрос: {}", o);
                });
    }
}
