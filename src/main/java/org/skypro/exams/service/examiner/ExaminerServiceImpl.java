// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.examiner;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.service.subjects.JavaQuestionService;
import org.skypro.exams.service.subjects.MathQuestionService;
import org.skypro.exams.service.subjects.QuestionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Реализация интерфейса экзаменатора {@link ExaminerService}.<br>
 * Возвращает список вопросов в заданном количестве.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public class ExaminerServiceImpl implements ExaminerService {

    @NotNull
    private final QuestionService javaQuestionService;

    @NotNull
    private final QuestionService mathQuestionService;

    @NotNull
    private final Random random;

    /**
     * Конструктор.
     *
     * @param javaQuestionService сервис вопросов по Java
     * @param mathQuestionService сервис вопросов по математике
     */
    public ExaminerServiceImpl(@NotNull final JavaQuestionService javaQuestionService,
                               @NotNull final MathQuestionService mathQuestionService) {
        this.javaQuestionService = javaQuestionService;
        this.mathQuestionService = mathQuestionService;
        random = new Random();
    }

    private int getQuestionsCount() {
        return javaQuestionService.getAmountOfQuestions() +
                mathQuestionService.getAmountOfQuestions();

    }

    /**
     * Возвращает заданное количество вопросов, набирая вопросы из разных предметов.
     *
     * @param amount количество вопросов
     * @return список вопросов
     */
    @Override
    @NotNull
    public final Collection<Question> getQuestions(final int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Количество вопросов должно быть больше или равно 1");
        }

        // Если вопрос только один,
        // то выбираем его из случайного предметного сервиса
        if (amount == 1) {
            QuestionService service = random.nextBoolean() ? javaQuestionService : mathQuestionService;
            return getQuestionsOf(service, amount);
        }

        // Если вопросов больше одного,
        // то выбираем половину из одного сервиса,
        // а вторую половину из другого
        int amountJava = amount / 2;
        int amountMath = amount - amountJava;

        return Stream.concat(
                getQuestionsOf(javaQuestionService, amountJava).stream(),
                getQuestionsOf(mathQuestionService, amountMath).stream()).toList();
    }

    private Collection<Question> getQuestionsOf(@NotNull final QuestionService questionService,
                                                final int amount) {
        List<Question> questions = new ArrayList<>(getQuestionsCount());

        int realAmount = questionService.getAmountOfQuestions();
        if (amount > realAmount) {
            throw new IllegalArgumentException("Количество вопросов не должно быть больше " + realAmount);
        }

        // Очень редкий случай, но не стоит по этой причине
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
