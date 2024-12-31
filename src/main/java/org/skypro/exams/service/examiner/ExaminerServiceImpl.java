// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.examiner;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.service.subjects.QuestionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Реализация интерфейса экзаменатора {@link ExaminerService}.<br>
 * Возвращает список вопросов в заданном количестве.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@Service
public class ExaminerServiceImpl implements ExaminerService {

    @NotNull
    private final Random random;

    @NotNull
    private final List<QuestionService> questionServices;

    /**
     * Конструктор.
     *
     * @param services сервисы вопросов
     */
    public ExaminerServiceImpl(@NotNull final QuestionService... services) {
        random = new Random();
        questionServices = List.of(services);
    }

    private int getQuestionsCount() {
        return questionServices.stream().mapToInt(QuestionService::getAmountOfQuestions).sum();
    }

    /**
     * Возвращает заданное количество вопросов, набирая вопросы из разных предметов
     * поровну по возможности.
     *
     * @param amount количество вопросов
     * @return список вопросов
     */
    @Override
    @NotNull
    public final Collection<Question> getQuestions(final int amount) {
        if (amount < 1) {
            return Collections.emptyList();
        }

        // Если вопрос только один, то
        // выбираем его из случайного предметного сервиса
        if (amount == 1) {
            QuestionService service = questionServices.get(random.nextInt(questionServices.size() - 1));
            return getQuestionsOf(service, amount);
        }

        // Если вопросов больше одного, то
        // набираем вопросы поровну из каждого сервиса.
        // Если amount меньше количества сервисов: например, amount == 3, а сервисов 5,
        // то из каждого сервиса выбираем по одному вопросу, пока не наберём 3 вопроса.

        int questionsPerService = Math.max(1, amount / questionServices.size());
        List<Question> yieldQuestions = new ArrayList<>(getQuestionsCount());

        int addition = amount % 2;
        if (addition > 0) {
            yieldQuestions.addAll(getQuestionsOf(questionServices.getFirst(), addition));
        }

        for (QuestionService service : questionServices) {
            yieldQuestions.addAll(getQuestionsOf(service, questionsPerService));
            if (yieldQuestions.size() >= amount) {
                break;
            }
        }

        return Collections.unmodifiableCollection(yieldQuestions);
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
