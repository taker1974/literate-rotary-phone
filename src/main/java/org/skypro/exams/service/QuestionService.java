// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service;

import org.skypro.exams.model.question.Question;

import java.util.Collection;

/**
 * Объявляет методы работы с вопросами определенного предмета.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public interface QuestionService {

    /**
     * @return коллекция вопросов {@link Question}
     */
    Collection<Question> getQuestionsAll();

    /**
     * Добавляет вопрос в хранилище.
     *
     * @param question экземпляр класса {@link Question}
     * @return возвращает добавленный вопрос
     * @throws org.skypro.exams.model.question.BadQuestionException в случае некорректного вопроса или если
     *                                                              корректный вопрос не может быть добавлен
     */
    Question addQuestion(Question question);

    /**
     * Добавляет вопрос в хранилище.
     *
     * @param questionText текст вопроса
     * @param answerText   текст ответа
     * @return возвращает добавленный вопрос {@link Question}
     * @throws org.skypro.exams.model.question.BadQuestionException в случае некорректного вопроса или если
     *                                                              корректный вопрос не может быть добавлен
     */
    Question addQuestion(String questionText, String answerText);

    /**
     * Удаляет вопрос из хранилища.
     *
     * @param question экземпляр класса {@link Question}
     * @return возвращает удалённый вопрос
     * @throws org.skypro.exams.model.question.BadQuestionException в случае некорректного вопроса или если
     *                                                              корректный вопрос не может быть удалён
     */
    Question removeQuestion(Question question);

    /**
     * @return случайный вопрос {@link Question}
     * @throws org.skypro.exams.model.question.BadQuestionException в случае, если вопросы недоступны
     */
    Question getRandomQuestion();
}
