// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.subjects;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.storage.QuestionRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Сервис для работы с вопросами по Java.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@Service
public final class JavaQuestionService extends BaseQuestionService {

    /**
     * Путь к файлу json с вопросами (default).
     */
    @NotNull
    public static final String JSON_QUESTIONS_PATH = "static/Questions-Java-Core.json";

    /**
     * Путь к файлу txt с вопросами (fallback).
     */
    @NotNull
    public static final String TEXT_QUESTIONS_PATH = "static/Questions-Java-Core.txt";

    /**
     * Конструктор.
     *
     * @param questionRepository репозиторий вопросов
     */
    public JavaQuestionService(@NotNull QuestionRepository questionRepository)
            throws URISyntaxException, IOException {

        super(questionRepository, JSON_QUESTIONS_PATH, TEXT_QUESTIONS_PATH);
    }
}
