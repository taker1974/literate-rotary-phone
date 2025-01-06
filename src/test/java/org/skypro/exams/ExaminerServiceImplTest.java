// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skypro.exams.model.question.BadQuestionException;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.model.storage.QuestionRepository;
import org.skypro.exams.model.storage.QuestionRepositoryException;
import org.skypro.exams.service.examiner.ExaminerServiceException;
import org.skypro.exams.service.examiner.ExaminerServiceImpl;
import org.skypro.exams.service.subjects.ArchQuestionService;
import org.skypro.exams.service.subjects.JavaQuestionService;
import org.skypro.exams.service.subjects.MathQuestionService;

/**
 * ExaminerServiceImplTest.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
class ExaminerServiceImplTest {

    private final QuestionRepository javaQuestionRepository;
    private final QuestionRepository mathQuestionRepository;
    private final QuestionRepository archQuestionRepository;

    private final ExaminerServiceImpl examinerService;

    private final QuestionRepository[] repositories;

    public ExaminerServiceImplTest() {
        javaQuestionRepository = new QuestionRepository();
        var javaQuestionService = new JavaQuestionService(javaQuestionRepository);

        mathQuestionRepository = new QuestionRepository();
        var mathQuestionService = new MathQuestionService(mathQuestionRepository);

        archQuestionRepository = new QuestionRepository();
        var archQuestionService = new ArchQuestionService(archQuestionRepository);

        examinerService = new ExaminerServiceImpl(
                javaQuestionService, mathQuestionService, archQuestionService);

        repositories = new QuestionRepository[]{
                javaQuestionRepository, mathQuestionRepository, archQuestionRepository};
    }

    private void clearRepositories() {
        for (var repository : repositories) {
            repository.clear();
        }
    }

    @Test
    void whenGetQuestionsAmountOutOfBounds_thenReturnsEmptyCollectionOrThrowsException() {
        clearRepositories();

        try {
            Assertions.assertEquals(0, examinerService.getQuestions(-1).size());
            Assertions.assertEquals(0, examinerService.getQuestions(0).size());
        } catch (ExaminerServiceException e) {
            Assertions.fail(examinerService.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        Assertions.assertThrows(ExaminerServiceException.class,
                () -> examinerService.getQuestions(1));

        // немного заполним хранилища вопросами
        final Question[] newQuestions;
        try {
            newQuestions = ExamsTestTools.getSomeQuestions();
            for (var question : newQuestions) {
                javaQuestionRepository.addQuestion(question);
                mathQuestionRepository.addQuestion(question);
                archQuestionRepository.addQuestion(question);
            }
        } catch (BadQuestionException | QuestionRepositoryException e) {
            Assertions.fail(ExamsTestTools.class.getName() + ", но здесь не должно быть ошибок");
            return;
        }

        // и повторим тесты
        try {
            Assertions.assertEquals(0, examinerService.getQuestions(-1).size());
            Assertions.assertEquals(0, examinerService.getQuestions(0).size());
        } catch (ExaminerServiceException e) {
            Assertions.fail(examinerService.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        Assertions.assertThrows(ExaminerServiceException.class,
                () -> examinerService.getQuestions(newQuestions.length * 3 + 1));
    }

    @Test
    void whenGetEquallyDivisibleAmount_thenReturnsEquallyDivisibleQuestions() {
        clearRepositories();

        // Заполним каждое хранилище вопросами вида "1-object:uuid", "1-object:uuid", .. "1-object"
        // где 1 .. 3 - префиксы хранилищ java, math. arch etc.
        final int[] repositoriesAmount = {10, 10, 10};
        try {
            for (int i = 0; i < repositories.length; i++) {
                var questions = ExamsTestTools.getPredictableQuestions(repositoriesAmount[i], i + 1 + "-object");
                for (var question : questions) {
                    repositories[i].addQuestion(question);
                }
            }
        } catch (BadQuestionException | QuestionRepositoryException e) {
            Assertions.fail(examinerService.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        // Попробуем выбрать 9 вопросов из 30: по 3 вопроса из каждого хранилища
        int amount = 9;
        try {
            var questions = examinerService.getQuestions(amount);

            // Проверим общее количество вопросов
            Assertions.assertEquals(amount, questions.size());

            // Проверим, что всех вопросов поровну, по 3 вопроса из каждого хранилища
            int c1 = 0, c2 = 0, c3 = 0;
            for (var question : questions) {
                if (question.getQuestionText().startsWith("1-")) {
                    c1++;
                }
                if (question.getQuestionText().startsWith("2-")) {
                    c2++;
                }
                if (question.getQuestionText().startsWith("3-")) {
                    c3++;
                }
            }
            Assertions.assertEquals(amount / repositories.length, c1);
            Assertions.assertEquals(amount / repositories.length, c2);
            Assertions.assertEquals(amount / repositories.length, c3);
        } catch (ExaminerServiceException e) {
            Assertions.fail(examinerService.getClass().getName() + ", но здесь не должно быть ошибок");
        }
    }

    /**
     * Из хранилищ по 10, 7 и 1 (18 всего) вопросов выберем "по 6" вопросов.
     * Алгоритм выборки вопросов сначала вычисляет условное количество вопросов на хранилище.
     * В данном случае это по 6 вопросов на хранилище. Строится таблица вида "хранилище -> количество вопросов".
     * Затем, в случае необходимости, таблица корректируется так, чтобы "разбросать" вопросы
     * по хранилищам, не превышая количество вопросов на хранилище. Недостача переносится на другие хранилища.
     * В результате должно получиться:
     * 10 (6+4) из хранилища 1
     * 7 (6+1)из хранилища 2
     * 1 (все) из хранилища 3
     */
    @Test
    void whenGetAmountEquallyDivisibleAmountFromNotEquallyRepos_thenReturnsProportionalAmountOfQuestions() {
        clearRepositories();

        // Заполним каждое хранилище вопросами вида "1-object:uuid", "1-object:uuid", .. "1-object"
        // где 1 .. 3 - префиксы хранилищ java, math. arch etc.
        final int[] repositoriesAmount = {10, 7, 1};
        try {
            for (int i = 0; i < repositories.length; i++) {
                var questions = ExamsTestTools.getPredictableQuestions(repositoriesAmount[i], i + 1 + "-object");
                for (var question : questions) {
                    repositories[i].addQuestion(question);
                }
            }
        } catch (BadQuestionException | QuestionRepositoryException e) {
            Assertions.fail(examinerService.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        // Попробуем выбрать все 18 вопросов из 18
        int amount = 18;
        try {
            var questions = examinerService.getQuestions(amount);

            // Проверим общее количество вопросов
            Assertions.assertEquals(amount, questions.size());

            // Проверим предположение о количестве вопросов из каждого хранилища (смотри выше)
            int c1 = 0, c2 = 0, c3 = 0;
            for (var question : questions) {
                if (question.getQuestionText().startsWith("1-")) {
                    c1++;
                }
                if (question.getQuestionText().startsWith("2-")) {
                    c2++;
                }
                if (question.getQuestionText().startsWith("3-")) {
                    c3++;
                }
            }
            Assertions.assertEquals(10, c1);
            Assertions.assertEquals(7, c2);
            Assertions.assertEquals(1, c3);
        } catch (ExaminerServiceException e) {
            Assertions.fail(examinerService.getClass().getName() + ", но здесь не должно быть ошибок");
        }
    }
}