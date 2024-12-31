// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.examiner;

import org.jetbrains.annotations.NotNull;

/**
 * Исключение работы с сервисом экзаменатора: слишком много вопросов запрошено.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public class TooManyQuestionsException extends RuntimeException {

    public TooManyQuestionsException(String message) {
        super(message);
    }

    public TooManyQuestionsException(@NotNull final Object o,
                                     final int requested, final int available) {

        super(o.getClass().getSimpleName() +
                ": Количество вопросов не должно быть больше " + available + ", но запрошено " + requested);
    }
}
