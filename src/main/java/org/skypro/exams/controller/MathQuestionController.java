// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.controller;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.BadQuestionException;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.model.storage.QuestionRepositoryException;
import org.skypro.exams.service.subjects.MathQuestionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Контроллер для работы с вопросами по математике.<br>
 * Позволяет пользователю добавлять, просматривать и удалять вопросы.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@RestController
@SuppressWarnings("unused") // ошибочное определение объекта кода, как неиспользуемого
public class MathQuestionController extends BaseQuestionController {

    /**
     * Конструктор.
     *
     * @param questionService сервис для работы с вопросами
     */
    public MathQuestionController(@NotNull final MathQuestionService questionService)
            throws QuestionRepositoryException {

        super(questionService);

        questionService.loadQuestions(
                MathQuestionService.JSON_QUESTIONS_PATH,
                MathQuestionService.TEXT_QUESTIONS_PATH);
    }

    /**
     * Добавление вопроса.
     *
     * @param questionText текст вопроса
     * @param answerText   текст ответа
     */
    @PutMapping("/exam/math/add")
    public void addQuestion(final String questionText, final String answerText) throws QuestionRepositoryException {
        questionService.addQuestion(questionText, answerText);
    }

    @PutMapping("/exam/math/save")
    public void saveQuestions() throws QuestionRepositoryException {
        questionService.saveQuestions(MathQuestionService.JSON_QUESTIONS_PATH);
    }

    /**
     * Получение всех вопросов.
     *
     * @return все вопросы
     */
    @RequestMapping("/exam/math")
    public Collection<Question> getQuestionsAll() {
        return questionService.getQuestionsAll();
    }

    /**
     * Удаление вопроса.
     *
     * @param questionText текст вопроса
     * @param answerText   текст ответа
     */
    @DeleteMapping("/exam/math/remove")
    public void removeQuestion(final String questionText, final String answerText)
            throws BadQuestionException, QuestionRepositoryException {
        super.removeQuestion(questionText, answerText);
    }
}
