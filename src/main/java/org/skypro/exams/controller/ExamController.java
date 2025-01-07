// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.controller;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.service.examiner.ExaminerService;
import org.skypro.exams.service.examiner.ExaminerServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Контроллер экзамена.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@RestController
@SuppressWarnings("unused") // ошибочное определение объекта кода, как неиспользуемого
@RequestMapping("/exam")
public class ExamController {

    @NotNull
    private final ExaminerService examinerService;

    public ExamController(@NotNull ExaminerService examinerService) {
        this.examinerService = examinerService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, world!";
    }

    @RequestMapping
    public Collection<Question> getQuestions(int amount) throws ExaminerServiceException {
        return examinerService.getQuestions(amount);
    }
}
