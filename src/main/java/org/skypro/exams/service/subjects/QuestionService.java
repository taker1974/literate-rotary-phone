// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.subjects;

import org.jetbrains.annotations.Nullable;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.model.storage.QuestionRepositoryException;

import java.util.Collection;

/**
 * Объявляет методы работы с вопросами определенного предмета.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.2
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
     * @return экземпляр класса {@link Question}
     * @throws QuestionRepositoryException если вопрос не может быть добавлен
     */
    Question addQuestion(Question question)
            throws QuestionRepositoryException;

    /**
     * Добавляет вопрос в хранилище.
     *
     * @param questionText текст вопроса
     * @param answerText   текст ответа
     * @return экземпляр класса {@link Question}
     * @throws QuestionRepositoryException если вопрос не может быть добавлен
     */
    Question addQuestion(String questionText, String answerText)
            throws QuestionRepositoryException;

    /**
     * Удаляет вопрос из хранилища.
     *
     * @param question экземпляр класса {@link Question}
     * @return экземпляр класса {@link Question}
     * @throws QuestionRepositoryException если вопрос не может быть добавлен
     */
    Question removeQuestion(Question question)
            throws QuestionRepositoryException;

    /**
     * @return случайный вопрос {@link Question}
     */
    Question getRandomQuestion()
            throws QuestionRepositoryException;

    /**
     * @return количество вопросов
     */
    int getAmountOfQuestions();

    /**
     * Загружает вопросы из файла.<br>
     * Если pathInResources равен null, то загружает вопросы из файла по умолчанию.
     *
     * @param jsonPathInResources путь к файлу json в ресурсах или null
     * @param textPathInResources путь к текстовому в ресурсах или null
     */
    void loadQuestions(@Nullable String jsonPathInResources, @Nullable String textPathInResources)
            throws QuestionRepositoryException;

    /**
     * Сохраняет вопросы в файл.<br>
     * Если jsonPathInResources равен null, то сохраняет вопросы в файл по умолчанию.
     *
     * @param jsonPathInResources путь к файлу в ресурсах или null
     */
    void saveQuestions(@Nullable String jsonPathInResources)
            throws QuestionRepositoryException;
}
