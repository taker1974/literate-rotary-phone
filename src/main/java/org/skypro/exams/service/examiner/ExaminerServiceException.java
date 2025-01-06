// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.examiner;

import org.jetbrains.annotations.NotNull;

/**
 * Исключение работы с сервисом экзаменатора: слишком много вопросов запрошено.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.2
 */
public class ExaminerServiceException extends Exception {

    public ExaminerServiceException(@NotNull final String message) {
        super(message);
    }
}
