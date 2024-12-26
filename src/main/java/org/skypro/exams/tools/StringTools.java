// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.tools;

/**
 * Инструменты работы с текстом.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public final class StringTools {

    /**
     * Конструктор.
     */
    private StringTools() {
    }

    /**
     * Проверка строки на соответствие требованиям.
     *
     * @param text      текст
     * @param minLength минимальная длина
     * @param maxLength максимальная длина
     * @return true, если строка соответствует требованиям
     */
    public static boolean isBadString(String text, int minLength, int maxLength) {
        if (text == null) {
            return true;
        }
        return text.length() < minLength ||
                text.length() > maxLength;
    }
}
