// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.examiner;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.Question;

import java.util.Collection;

/**
 * Сервис для экзаменаторов.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public interface ExaminerService {

    /**
     * Получить вопросы для экзаменатора.
     *
     * @param amount количество вопросов
     * @return вопросы
     */
    @NotNull
    Collection<Question> getQuestions(final int amount) throws ExaminerServiceException;
}
