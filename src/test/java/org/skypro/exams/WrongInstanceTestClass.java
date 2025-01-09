// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2025
// Курсовая работа. Java Core.

package org.skypro.exams;

import org.jetbrains.annotations.NotNull;

/**
 * WrongInstanceTestClass - тестовый класс общего назначения.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2025
 * @version 0.1
 */
public final class WrongInstanceTestClass {

    @NotNull
    private final String name;

    public WrongInstanceTestClass(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return this.name;
    }
}
