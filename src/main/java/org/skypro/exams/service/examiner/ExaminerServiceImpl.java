// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.examiner;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.service.ExaminerService;
import org.skypro.exams.service.QuestionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Реализация интерфейса экзаменатора {@link ExaminerService}.<br>
 * Возвращает список вопросов в заданном количестве.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public class ExaminerServiceImpl implements ExaminerService {

    @NotNull
    private final QuestionService questionService;

    public ExaminerServiceImpl(@NotNull QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    @NotNull
    public final Collection<Question> getQuestions(final int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Количество вопросов должно быть больше 0");
        }
        int realAmount = questionService.getAmountOfQuestions();
        if (amount > realAmount) {
            throw new IllegalArgumentException("Количество вопросов не должно быть больше " + realAmount);
        }

        List<Question> questions = new ArrayList<>();

        // очень редкий случай, но не стоит по этой причине
        // начинать играть в кости
        if (amount == realAmount) {
            questions.addAll(questionService.getQuestionsAll());
            return Collections.unmodifiableCollection(questions);
        }

        while (questions.size() < amount) {
            var question = questionService.getRandomQuestion();
            if (!questions.contains(question)) {
                questions.add(question);
            }
        }

        return Collections.unmodifiableCollection(questions);
    }
}
