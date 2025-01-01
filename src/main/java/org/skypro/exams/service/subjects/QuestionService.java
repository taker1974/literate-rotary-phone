// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.subjects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skypro.exams.model.question.Question;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
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
     * @throws org.skypro.exams.model.question.BadQuestionException в случае некорректного вопроса или если
     *                                                              корректный вопрос не может быть добавлен
     */
    void addQuestion(Question question);

    /**
     * Добавляет вопрос в хранилище.
     *
     * @param questionText текст вопроса
     * @param answerText   текст ответа
     * @throws org.skypro.exams.model.question.BadQuestionException в случае некорректного вопроса или если
     *                                                              корректный вопрос не может быть добавлен
     */
    void addQuestion(String questionText, String answerText);

    /**
     * Удаляет вопрос из хранилища.
     *
     * @param question экземпляр класса {@link Question}
     * @throws org.skypro.exams.model.question.BadQuestionException в случае некорректного вопроса или если
     *                                                              корректный вопрос не может быть удалён
     */
    void removeQuestion(Question question);

    /**
     * @return случайный вопрос {@link Question}
     * @throws org.skypro.exams.model.question.BadQuestionException в случае, если вопросы недоступны
     */
    Question getRandomQuestion();

    /**
     * @return количество вопросов
     */
    int getAmountOfQuestions();

    /**
     * Загружает вопросы из файла.<br>
     * Если pathInResources равен null, то загружает вопросы из файла по умолчанию.
     *
     * @param jsonPathInResources путь к файлу json в ресурсах или null
     * @param textPathInResources  путь к текстовому в ресурсах или null
     */
    void loadQuestions(@Nullable String jsonPathInResources, @Nullable String textPathInResources) throws URISyntaxException, IOException;

    /**
     * Сохраняет вопросы в файл.<br>
     * Если jsonPathInResources равен null, то сохраняет вопросы в файл по умолчанию.
     *
     * @param jsonPathInResources путь к файлу в ресурсах или null
     */
    void saveQuestions(@Nullable String jsonPathInResources) throws IOException, URISyntaxException;
}
