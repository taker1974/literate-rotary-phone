// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JavaQuestionServiceTest {

    private final boolean x;

    public JavaQuestionServiceTest() {
        x = false;
    }

    @Test
    void whenGet_thenReturns() {
        Assertions.assertTrue(x);
    }
}
