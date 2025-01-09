// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.exams.model.question.BadQuestionException;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.model.storage.QuestionRepository;
import org.skypro.exams.model.storage.QuestionRepositoryException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import static org.skypro.exams.ExamsTestTools.getSomeQuestions;

/**
 * QuestionRepositoryTest.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.3
 */
@ExtendWith(MockitoExtension.class)
class QuestionRepositoryTest {

    /**
     * Ожидаемое количество вопросов и ответов после загрузки репозитория из
     * текстового файла static/Questions-Java-Core.txt.<br>
     * На момент написания теста в этом файле QUESTIONS_COUNT вопросов и ответов.
     */
    public static final int QUESTIONS_COUNT = 27;

    @Test
    void whenLoadQuestionsFromTextFile_thenQuestionsAreLoaded() {
        QuestionRepository questionRepository = new QuestionRepository();

        // проверяем работу с "кривыми" именами файлов при загрузке из файла
        Assertions.assertThrows(Exception.class, () ->
                questionRepository.loadQuestionsFromTextFile(null));
        Assertions.assertThrows(Exception.class, () ->
                questionRepository.loadQuestionsFromTextFile(""));
        Assertions.assertThrows(Exception.class, () ->
                questionRepository.loadQuestionsFromTextFile("bad.file.name"));

        final String goodFileName = "static/Questions-Java-Core.txt";
        try {
            questionRepository.loadQuestionsFromTextFile(goodFileName);
        } catch (QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }

        // проверяем количество вопросов
        Assertions.assertEquals(QUESTIONS_COUNT,
                questionRepository.getQuestionsAll().size());
    }

    @Test
    void whenLoadQuestionsFromJson_thenQuestionsAreLoaded() {
        QuestionRepository questionRepository = new QuestionRepository();

        // проверяем работу с "кривыми" именами файлов при загрузке из файла
        Assertions.assertThrows(QuestionRepositoryException.class, () ->
                questionRepository.loadQuestionsFromJsonFile(null));
        Assertions.assertThrows(QuestionRepositoryException.class, () ->
                questionRepository.loadQuestionsFromJsonFile(""));
        Assertions.assertThrows(QuestionRepositoryException.class, () ->
                questionRepository.loadQuestionsFromJsonFile("bad.file.name"));

        final String goodFileName = "static/Questions-Java-Core.json";
        try {
            questionRepository.loadQuestionsFromJsonFile(goodFileName);
        } catch (QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }

        // проверяем количество вопросов
        Assertions.assertEquals(QUESTIONS_COUNT,
                questionRepository.getQuestionsAll().size());
    }

    @Test
    void whenSaveQuestionsToJson_thenQuestionsAreSaved() {
        QuestionRepository questionRepository = new QuestionRepository();

        // сначала наполняем репозиторий
        final String goodSourceFileName = "static/Questions-Java-Core.json";
        try {
            questionRepository.loadQuestionsFromJsonFile(goodSourceFileName);
        } catch (QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }

        // проверяем работу с "кривыми" именами файлов при сохранении в файл
        Assertions.assertThrows(QuestionRepositoryException.class, () ->
                questionRepository.saveQuestionsToJson(null));
        Assertions.assertThrows(QuestionRepositoryException.class, () ->
                questionRepository.saveQuestionsToJson(""));

        // сохраняем вопросы в файл с уникальным именем в директорию ресурсов (в target/, конечно)
        final String goodTargetFileName = "static/ForRemove-" + UUID.randomUUID() + "-Questions-Java-Core.json";
        try {
            questionRepository.saveQuestionsToJson(goodTargetFileName);
        } catch (QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }

        // проверяем существование файла
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final URI resourceUri;
        final Path fullPath;
        try {
            resourceUri = Objects.requireNonNull(classloader.getResource("")).toURI();
            fullPath = Paths.get(resourceUri).resolve(goodTargetFileName);
            Assertions.assertFalse(fullPath.toString().isEmpty());
        } catch (URISyntaxException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        Assertions.assertTrue(Files.exists(fullPath));

        // удаляем файл
        try {
            Files.delete(fullPath);
        } catch (IOException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }
    }

    @Test
    void whenGetQuestions_thenQuestionsAreReturned() {
        QuestionRepository questionRepository = new QuestionRepository();

        final String goodSourceFileName = "static/Questions-Java-Core.json";
        try {
            questionRepository.loadQuestionsFromJsonFile(goodSourceFileName);
        } catch (QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }

        Assertions.assertEquals(QUESTIONS_COUNT, questionRepository.getQuestionsAll().size());
    }

    @Test
    void whenAddQuestion_thenQuestionIsAdded() {
        QuestionRepository questionRepository = new QuestionRepository();

        // загружаем вопросы из файла
        final String goodSourceFileName = "static/Questions-Java-Core.json";
        try {
            questionRepository.loadQuestionsFromJsonFile(goodSourceFileName);
        } catch (QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

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

        // проверяем количество вопросов
        Assertions.assertEquals(QUESTIONS_COUNT + newQuestions.length,
                questionRepository.getQuestionsAll().size());

        // проверяем существование вопросов, добавленных вручную
        Assertions.assertTrue(questionRepository.getQuestionsAll()
                .contains(newQuestions[0]));
        Assertions.assertTrue(questionRepository.getQuestionsAll()
                .contains(newQuestions[1]));
    }

    @Test
    void whenRemoveQuestion_thenQuestionIsRemoved() {
        QuestionRepository questionRepository = new QuestionRepository();

        // загружаем вопросы из файла
        final String goodSourceFileName = "static/Questions-Java-Core.json";
        try {
            questionRepository.loadQuestionsFromJsonFile(goodSourceFileName);
        } catch (QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

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

        try {
            questionRepository.removeQuestion(newQuestions[0]);
        } catch (QuestionRepositoryException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
            return;
        }

        Assertions.assertEquals(QUESTIONS_COUNT + newQuestions.length - 1,
                questionRepository.getQuestionsAll().size());

        Assertions.assertFalse(questionRepository.getQuestionsAll()
                .contains(newQuestions[0]));

        Assertions.assertTrue(questionRepository.getQuestionsAll()
                .contains(newQuestions[1]));
    }

    @Test
    void whenClear_thenAllQuestionsAreRemoved() {
        QuestionRepository questionRepository = new QuestionRepository();

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

        Assertions.assertEquals(newQuestions.length,
                questionRepository.getQuestionsAll().size());

        questionRepository.clear();

        Assertions.assertEquals(0, questionRepository.getQuestionsAll().size());
    }
}
