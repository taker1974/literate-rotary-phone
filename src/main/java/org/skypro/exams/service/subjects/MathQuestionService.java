// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.subjects;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.storage.QuestionRepository;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с вопросами по математике.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@Service
public final class MathQuestionService extends BaseQuestionService {

    /**
     * Путь к файлу json с вопросами (default).
     */
    @NotNull
    public static final String JSON_QUESTIONS_PATH = "static/Questions-Math.json";

    /**
     * Путь к файлу txt с вопросами (fallback).
     */
    @NotNull
    public static final String TEXT_QUESTIONS_PATH = "static/Questions-Math.txt";

    /**
     * Конструктор.
     *
     * @param questionRepository репозиторий вопросов
     */
    public MathQuestionService(@NotNull QuestionRepository questionRepository) {
        super(questionRepository);
    }
}
