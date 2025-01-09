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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация интерфейса экзаменатора {@link ExaminerService}.<br>
 * Возвращает список вопросов в заданном количестве.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@Service
@SuppressWarnings("unused") // ошибочное определение объекта кода, как неиспользуемого
public class ExaminerServiceImpl implements ExaminerService {

    @NotNull
    private final List<QuestionService> questionServices;

    /**
     * Конструктор.
     *
     * @param services сервисы вопросов
     */
    public ExaminerServiceImpl(@NotNull final QuestionService... services) {
        questionServices = List.of(services);
    }

    private int getQuestionsCount() {
        return questionServices.stream()
                .mapToInt(QuestionService::getAmountOfQuestions)
                .sum();
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
    public final Collection<Question> getQuestions(final int amount) throws ExaminerServiceException {
        if (amount < 1) {
            return Collections.emptyList();
        }

        int questionsAvailable = getQuestionsCount();
        if (amount > questionsAvailable) {
            throw new ExaminerServiceException(this.getClass().getName() +
                    ": Количество вопросов не должно быть больше " + questionsAvailable +
                    ", но запрошено " + amount);
        }

        int servicesCount = questionServices.size();

        // По возможности, распределяем вопросы поровну
        int questionsPerService = Math.max(1, amount / servicesCount);

        // Если поровну не получается, то запоминаем остаток
        int addition = amount % servicesCount;

        // Возвращаемая коллекция вопросов
        List<Question> yieldQuestions = new ArrayList<>(questionsAvailable);

        // Таблица "сервис вопросов -> количество запрашиваемых вопросов"
        var workTable = getWorkTable(questionsPerService, questionServices, addition);

        // Выполняем задание по получению требуемого количества вопросов
        // из каждого сервиса
        for (var entry : workTable.entrySet()) {
            QuestionService service = entry.getKey();
            int wanted = entry.getValue();

            yieldQuestions.addAll(getQuestionsOf(service, wanted, GetQuestionsPolicy.GET_ALL_AVAILABLE));
            if (yieldQuestions.size() >= amount) {
                break;
            }
        }

        return Collections.unmodifiableCollection(yieldQuestions);
    }

    @NotNull
    private static HashMap<QuestionService, Integer> getWorkTable(final int questionsPerService,
                                                                  final List<QuestionService> services,
                                                                  int addition) {
        final int servicesCount = services.size();

        // Пишем в таблицу "сервис вопросов -> количество запрашиваемых вопросов"
        // количество вопросов, которое нужно получить из каждого сервиса
        HashMap<QuestionService, Integer> workTable = HashMap.newHashMap(servicesCount);
        for (QuestionService service : services) {
            // Сначала и по умолчанию пишем кол-во вопросов
            // на основании деления поровну
            workTable.put(service, questionsPerService);

            // Если в сервисе вопросов меньше, чем требуется, то
            // недостающее число запрашиваемых вопросов суммируем с ранее полученным
            // остатком, а в таблице меняем количество вопросов на максимально доступное
            int delta = service.getAmountOfQuestions() - questionsPerService;
            if (delta < 0) {
                workTable.put(service, questionsPerService + delta);
                addition += Math.abs(delta);
            }
        }

        // Распределяем остатки количества вопросов по сервисам
        while (addition > 0) {
            for (var entry : workTable.entrySet()) {
                int available = getAvailableQuestions(entry);
                if (available > 0) {
                    var workValue = entry.getValue();
                    workTable.put(entry.getKey(), ++workValue);
                    addition--;
                    if (addition <= 0) {
                        break;
                    }
                }
            }
        }

        return workTable;
    }

    private static int getAvailableQuestions(@NotNull Map.Entry<QuestionService, Integer> entry) {
        return entry.getKey().getAmountOfQuestions() - entry.getValue();
    }

    /**
     * Политика получения вопросов.
     */
    private enum GetQuestionsPolicy {
        FAIL_ON_BAD_AMOUNT,
        GET_ALL_AVAILABLE,
    }

    @SuppressWarnings("SameParameterValue") // policy пока вызывается только с GET_ALL_AVAILABLE
    private Collection<Question> getQuestionsOf(@NotNull final QuestionService questionService,
                                                final int amount, GetQuestionsPolicy policy)
            throws ExaminerServiceException {

        List<Question> questions = new ArrayList<>(getQuestionsCount());

        int available = questionService.getAmountOfQuestions();
        if (available < amount && policy == GetQuestionsPolicy.FAIL_ON_BAD_AMOUNT) {
            throw new ExaminerServiceException(questionService.getClass().getName() +
                    ": Количество вопросов не должно быть больше " + available +
                    ", но запрошено " + amount);
        }

        // Очень редкий случай, но не стоит по этой причине
        // начинать играть в кости
        if (amount == available) {
            questions.addAll(questionService.getQuestionsAll());
            return Collections.unmodifiableCollection(questions);
        }

        if (amount > available && policy == GetQuestionsPolicy.GET_ALL_AVAILABLE) {
            questions.addAll(questionService.getQuestionsAll());
            return Collections.unmodifiableCollection(questions);
        }

        try {
            while (questions.size() < amount) {
                var question = questionService.getRandomQuestion();
                if (!questions.contains(question)) {
                    questions.add(question);
                }
            }
        } catch (Exception e) {
            throw new ExaminerServiceException(e.getMessage());
        }

        return Collections.unmodifiableCollection(questions);
    }
}
