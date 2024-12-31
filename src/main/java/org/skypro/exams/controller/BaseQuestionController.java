// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.controller;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.service.subjects.JavaQuestionService;
import org.skypro.exams.service.subjects.QuestionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Базовый класс для контроллеров работы с вопросами.<br>
 * Позволяет пользователю добавлять, просматривать и удалять вопросы.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public abstract class BaseQuestionController {

    @NotNull
    protected final QuestionService questionService;

    /**
     * Конструктор.
     *
     * @param questionService сервис для работы с вопросами
     */
    protected BaseQuestionController(@NotNull final QuestionService questionService) {
        this.questionService = questionService;
    }
}
