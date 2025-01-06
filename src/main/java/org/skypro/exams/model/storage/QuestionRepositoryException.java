// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.model.storage;

/**
 * Исключение работы с хранилищем вопросов.<br>
 * В основном для трансляции множества библиотечных исключений URI, файлов и тому подобное
 * в единое прикладное исключение хранилища.<br>
 * <b>Проверяемое исключение</b>.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public class QuestionRepositoryException extends Exception {
    public QuestionRepositoryException(String message) {

        super(message);
    }
}
