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
        try {
            for (int i = 0; i < repositories.length; i++) {
                var questions = ExamsTestTools.getPredictableQuestions(10, i + 1 + "-object");
                for (var question : questions) {
                    repositories[i].addQuestion(question);
                }
            }
        } catch (BadQuestionException | QuestionRepositoryException e) {
            Assertions.fail(examinerService.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        // Попробуем выбрать 9 вопросов
        try {
            var questions = examinerService.getQuestions(9);

            // Проверим общее количество вопросов
            Assertions.assertEquals(9, questions.size());

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
            Assertions.assertEquals(3, c1);
            Assertions.assertEquals(3, c2);
            Assertions.assertEquals(3, c3);
        } catch (ExaminerServiceException e) {
            Assertions.fail(examinerService.getClass().getName() + ", но здесь не должно быть ошибок");
        }
    }
}