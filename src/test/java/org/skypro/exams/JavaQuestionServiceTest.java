// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.exams.model.question.BadQuestionException;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.model.storage.QuestionRepository;
import org.skypro.exams.model.storage.QuestionRepositoryException;
import org.skypro.exams.service.subjects.JavaQuestionService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import static org.skypro.exams.ExamsTestTools.getSomeQuestions;

/**
 * QuestionRepositoryTest.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@ExtendWith(MockitoExtension.class)
class JavaQuestionServiceTest {

    @Mock
    private final Random random;

    private final QuestionRepository questionRepository;
    private final JavaQuestionService questionService;

    public JavaQuestionServiceTest() {
        random = new Random();

        questionRepository = new QuestionRepository();
        questionService = new JavaQuestionService(questionRepository);
    }

    @Test
    void whenGetQuestionsAll_thenReturnQuestionsAll() {
        questionRepository.clear();
        Assertions.assertEquals(0, questionService.getQuestionsAll().size());

        // добавляем пару вопросов вручную
        final Question[] newQuestions;
        try {
            newQuestions = getSomeQuestions();
            for (var question : newQuestions) {
                questionRepository.addQuestion(question);
            }
        } catch (BadQuestionException | QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        Assertions.assertEquals(newQuestions.length, questionService.getQuestionsAll().size());
    }

    @Test
    void whenAddQuestionWithInstance_thenQuestionAdded() {
        questionRepository.clear();
        Assertions.assertEquals(0, questionService.getQuestionsAll().size());

        Assertions.assertThrows(NullPointerException.class, () ->
                questionService.addQuestion(null));

        // добавляем пару вопросов вручную
        final Question[] newQuestions;
        try {
            newQuestions = getSomeQuestions();
            for (var question : newQuestions) {
                questionRepository.addQuestion(question);
            }
        } catch (BadQuestionException | QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        Assertions.assertEquals(newQuestions.length, questionService.getQuestionsAll().size());
    }

    @Test
    void whenAddQuestionWithText_thenQuestionAdded() {
        questionRepository.clear();
        Assertions.assertEquals(0, questionService.getQuestionsAll().size());

        Assertions.assertThrows(QuestionRepositoryException.class, () ->
                questionService.addQuestion(null, null));
        Assertions.assertThrows(QuestionRepositoryException.class, () ->
                questionService.addQuestion("", null));
        Assertions.assertThrows(QuestionRepositoryException.class, () ->
                questionService.addQuestion("", ""));
        Assertions.assertThrows(QuestionRepositoryException.class, () ->
                questionService.addQuestion("Вопрос без ответа", ""));
        Assertions.assertThrows(QuestionRepositoryException.class, () ->
                questionService.addQuestion(null, "Ответ без вопроса"));

        // добавляем пару вопросов вручную
        final Question[] newQuestions;
        try {
            newQuestions = getSomeQuestions();
            for (var question : newQuestions) {
                questionRepository.addQuestion(question);
            }
        } catch (BadQuestionException | QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        Assertions.assertEquals(newQuestions.length, questionService.getQuestionsAll().size());
    }

    @Test
    void whenRemoveQuestionWithInstance_thenQuestionRemoved() {
        questionRepository.clear();
        Assertions.assertEquals(0, questionService.getQuestionsAll().size());

        // добавляем пару вопросов вручную
        final Question[] newQuestions;
        try {
            newQuestions = getSomeQuestions();
            for (var question : newQuestions) {
                questionRepository.addQuestion(question);
            }
        } catch (BadQuestionException | QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        Assertions.assertThrows(NullPointerException.class, () ->
                questionService.removeQuestion(null));

        try {
            questionService.removeQuestion(newQuestions[0]);
        } catch (QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        Assertions.assertEquals(newQuestions.length - 1, questionService.getQuestionsAll().size());
    }

    @Test
    void whenGetRandomQuestion_thenReturnRandomQuestion() {
        questionRepository.clear();

        // добавляем пару вопросов вручную
        final Question[] newQuestions;
        try {
            newQuestions = getSomeQuestions();
            for (var question : newQuestions) {
                questionRepository.addQuestion(question);
            }
        } catch (BadQuestionException | QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        // добавляем несколько вопросов вручную в эталонное хранилище
        final Question[] newStandardQuestions;
        try {
            newStandardQuestions = getSomeQuestions();
            for (var question : newQuestions) {
                questionRepository.addQuestion(question);
            }
        } catch (BadQuestionException | QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        HashSet<Question> standardRepository = HashSet.newHashSet(newStandardQuestions.length);
        Collections.addAll(standardRepository, newStandardQuestions);

        int randomMax = questionRepository.getQuestionsAll().size();

        try {
            Mockito.when(random.nextInt(1, randomMax)).thenReturn(1);
            Assertions.assertEquals(questionService.getRandomQuestion(),
                    ExamsTestTools.getAt(standardRepository, 0));

            Mockito.when(random.nextInt(1, randomMax)).thenReturn(randomMax);
            Assertions.assertEquals(questionService.getRandomQuestion(),
                    ExamsTestTools.getAt(standardRepository, randomMax - 1));

            Mockito.when(random.nextInt(1, randomMax)).thenReturn(randomMax / 2);
            Assertions.assertEquals(questionService.getRandomQuestion(),
                    ExamsTestTools.getAt(standardRepository, (randomMax / 2) - 1));
        } catch (QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }
    }
}
