// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.model.question;

/**
 * Исключение работы с вопросом: нулевые или пустые строки, строки слишком большой длины.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public class BadQuestionException extends RuntimeException {
    public BadQuestionException(String message) {

        super(message);
    }
}
