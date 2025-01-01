// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.controller;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.service.subjects.ArchQuestionService;
import org.skypro.exams.service.subjects.JavaQuestionService;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.Collection;
import java.util.List;

/**
 * Контроллер для работы с вопросами по архитектуре ПО.<br>
 * Позволяет пользователю добавлять, просматривать и удалять вопросы.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@RestController
public class ArchQuestionController extends BaseQuestionController {

    /**
     * Конструктор.
     *
     * @param questionService сервис для работы с вопросами
     */
    public ArchQuestionController(@NotNull final ArchQuestionService questionService) {

        super(questionService);
        // Здесь мы не станем загружать вопросы и ответы, так как этот контроллер
        // должен порождать ошибки
    }

    /**
     * Добавление вопроса.
     *
     * @param questionText текст вопроса
     * @param answerText   текст ответа
     */
    @PutMapping("/exam/arch/add")
    public void addQuestion(final String questionText, final String answerText) {
        throw new MethodNotAllowedException(HttpMethod.PUT, List.of(HttpMethod.HEAD));
    }

    /**
     * Получение всех вопросов.
     *
     * @return все вопросы
     */
    @RequestMapping("/exam/arch")
    public Collection<Question> getQuestionsAll() {
        throw new MethodNotAllowedException(HttpMethod.GET, List.of(HttpMethod.HEAD));
    }

    /**
     * Удаление вопроса.
     *
     * @param questionText текст вопроса
     * @param answerText   текст ответа
     */
    @DeleteMapping("/exam/arch/remove")
    public void removeQuestion(final String questionText, final String answerText) {
        throw new MethodNotAllowedException(HttpMethod.DELETE, List.of(HttpMethod.HEAD));
    }
}
