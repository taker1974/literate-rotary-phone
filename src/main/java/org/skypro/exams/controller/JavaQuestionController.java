// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.controller;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.BadQuestionException;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.model.storage.QuestionRepositoryException;
import org.skypro.exams.service.subjects.JavaQuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Контроллер для работы с вопросами по Java.<br>
 * Позволяет пользователю добавлять, просматривать и удалять вопросы.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@RestController
@SuppressWarnings("unused") // ошибочное определение объекта кода, как неиспользуемого
@RequestMapping("/java")
public class JavaQuestionController extends BaseQuestionController {

    /**
     * Конструктор.
     *
     * @param questionService сервис для работы с вопросами
     */
    public JavaQuestionController(@NotNull final JavaQuestionService questionService)
            throws QuestionRepositoryException {

        super(questionService);

        questionService.loadQuestions(
                JavaQuestionService.JSON_QUESTIONS_PATH,
                JavaQuestionService.TEXT_QUESTIONS_PATH);
    }

    /**
     * Добавление вопроса.
     *
     * @param questionText текст вопроса
     * @param answerText   текст ответа
     */
    @PutMapping("/add")
    public ResponseEntity<Question> addQuestion(
            @RequestParam(name = "question") final String questionText,
            @RequestParam(name = "answer") final String answerText)
            throws QuestionRepositoryException {
        Question question = questionService.addQuestion(questionText, answerText);
        return ResponseEntity.ok(question);
    }

    @PutMapping("/save")
    public ResponseEntity<Boolean> saveQuestions() throws QuestionRepositoryException {
        questionService.saveQuestions(JavaQuestionService.JSON_QUESTIONS_PATH);
        return ResponseEntity.ok(true);
    }

    /**
     * Получение всех вопросов.
     *
     * @return все вопросы
     */
    @RequestMapping
    public Collection<Question> getQuestionsAll() {
        return questionService.getQuestionsAll();
    }

    /**
     * Удаление вопроса.
     *
     * @param questionText текст вопроса
     * @param answerText   текст ответа
     */
    @DeleteMapping("/remove")
    @Override
    public ResponseEntity<Question> removeQuestion(
            @RequestParam(name = "question") final String questionText,
            @RequestParam(name = "answer") final String answerText)
            throws QuestionRepositoryException, BadQuestionException {
        return super.removeQuestion(questionText, answerText);
    }
}
