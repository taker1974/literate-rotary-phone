// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.controller;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.service.examiner.ExaminerService;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

/**
 * Контроллер экзамена.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public class ExamController {

    @NotNull
    private final ExaminerService examinerService;

    public ExamController(@NotNull ExaminerService examinerService) {
        this.examinerService = examinerService;
    }

    @RequestMapping("/exam/get?amount")
    public Collection<Question> getQuestions(int amount) {
        return examinerService.getQuestions(amount);
    }
}
