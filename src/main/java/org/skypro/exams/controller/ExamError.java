// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.controller;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Ошибка сервиса экзаменатора.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public final class ExamError {

    @NotNull
    @Getter
    private final String code;

    @NotNull
    @Getter
    private final String message;

    public ExamError(@NotNull String code, @NotNull String message) {
        this.code = code;
        this.message = message;
    }
}
