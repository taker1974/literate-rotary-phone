// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.subjects;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.storage.QuestionRepository;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с вопросами по архитектуре ПО.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@Service
public final class ArchQuestionService extends BaseQuestionService {

    /**
     * Путь к файлу json с вопросами (default).
     */
    @NotNull
    @SuppressWarnings("unused") // не должно использоваться, так как относится к заглушке
    public static final String JSON_QUESTIONS_PATH = "static/Questions-Arch.json";

    /**
     * Путь к файлу txt с вопросами (fallback).
     */
    @NotNull
    @SuppressWarnings("unused") // не должно использоваться, так как относится к заглушке
    public static final String TEXT_QUESTIONS_PATH = "static/Questions-Arch.txt";

    /**
     * Конструктор.
     *
     * @param questionRepository репозиторий вопросов
     */
    public ArchQuestionService(@NotNull QuestionRepository questionRepository) {
        super(questionRepository);
    }
}
