// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.controller;

import org.jetbrains.annotations.NotNull;

/**
 * Ошибка сервиса экзаменатора.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.2
 */
public record ExamError(@NotNull String code, @NotNull String message) {
}
