// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.model.storage.QuestionRepository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

/**
 * QuestionRepositoryTest.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@ExtendWith(MockitoExtension.class)
class QuestionRepositoryTest {

    /**
     * Ожидаемое количество вопросов и ответов после загрузки репозитория.<br>
     * На момент написания теста в этом файле 29 вопросов и ответов.
     */
    public static final int QUESTIONS_COUNT = 29;

    @Test
    void whenLoadQuestionFromTextFile_thenQuestionsAreLoaded() {
        QuestionRepository questionRepository = new QuestionRepository();

        // проверяем работу с "кривыми" именами файлов при загрузке из файла
        Assertions.assertThrows(RuntimeException.class, () ->
                questionRepository.loadQuestionsFromTextFile(null));
        Assertions.assertThrows(RuntimeException.class, () ->
                questionRepository.loadQuestionsFromTextFile(""));
        Assertions.assertThrows(RuntimeException.class, () ->
                questionRepository.loadQuestionsFromTextFile("bad.file.name"));

        final String goodFileName = "static/Questions-Java-Core.txt";
        try {
            questionRepository.loadQuestionsFromTextFile(goodFileName);
        } catch (URISyntaxException | IOException | RuntimeException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }

        // проверяем количество вопросов
        Assertions.assertEquals(QUESTIONS_COUNT,
                questionRepository.getQuestionsAll().size());
    }

    @Test
    void whenLoadQuestionFromJson_thenQuestionsAreLoaded() {
        QuestionRepository questionRepository = new QuestionRepository();

        // проверяем работу с "кривыми" именами файлов при загрузке из файла
        Assertions.assertThrows(RuntimeException.class, () ->
                questionRepository.loadQuestionsFromJsonFile(null));
        Assertions.assertThrows(RuntimeException.class, () ->
                questionRepository.loadQuestionsFromJsonFile(""));
        Assertions.assertThrows(RuntimeException.class, () ->
                questionRepository.loadQuestionsFromJsonFile("bad.file.name"));

        final String goodFileName = "static/Questions-Java-Core.json";
        try {
            questionRepository.loadQuestionsFromJsonFile(goodFileName);
        } catch (URISyntaxException | IOException | RuntimeException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }

        // проверяем количество вопросов
        Assertions.assertEquals(QUESTIONS_COUNT,
                questionRepository.getQuestionsAll().size());
    }

    @Test
    // для Paths.get().resolve():
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void whenSaveQuestionToJson_thenQuestionsAreSaved() {
        QuestionRepository questionRepository = new QuestionRepository();

        // сначала наполняем репозиторий
        final String goodSourceFileName = "static/Questions-Java-Core.json";
        try {
            questionRepository.loadQuestionsFromJsonFile(goodSourceFileName);
        } catch (URISyntaxException | IOException | RuntimeException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }

        // проверяем работу с "кривыми" именами файлов при сохранении в файл
        Assertions.assertThrows(RuntimeException.class, () ->
                questionRepository.saveQuestionsToJson(null));
        Assertions.assertThrows(RuntimeException.class, () ->
                questionRepository.saveQuestionsToJson(""));

        // сохраняем вопросы в файл с уникальным именем в директорию ресурсов (в target/, конечно)
        final String goodTargetFileName = "static/ForRemove-" + UUID.randomUUID() + "-Questions-Java-Core.json";
        try {
            questionRepository.saveQuestionsToJson(goodTargetFileName);
        } catch (URISyntaxException | IOException | RuntimeException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }

        // проверяем существование файла
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            final URI resourceUri = Objects.requireNonNull(classloader.getResource("")).toURI();
            Paths.get(resourceUri).resolve(goodTargetFileName);
        } catch (URISyntaxException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }

        Path path = Paths.get(goodTargetFileName);
        Assertions.assertTrue(Files.exists(path));

        // удаляем файл
        try {
            Files.delete(path);
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
        } catch (URISyntaxException | IOException | RuntimeException e) {
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
        } catch (URISyntaxException | IOException | RuntimeException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }

        // добавляем пару вопросов вручную
        var newQuestions = new Question[]{
                new Question("Why?", "Just because"),
                new Question("Why so serious?", "I'm so happy about it")
        };

        questionRepository.addQuestion(newQuestions[0]);
        questionRepository.addQuestion(newQuestions[1]);

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
        } catch (URISyntaxException | IOException | RuntimeException e) {
            Assertions.fail(e.getClass().getName() + ", но здесь не должно быть ошибок");
        }

        // добавляем пару вопросов вручную
        var newQuestions = new Question[]{
                new Question("Why?", "Just because"),
                new Question("Why so serious?", "I'm so happy about it")
        };

        questionRepository.addQuestion(newQuestions[0]);
        questionRepository.addQuestion(newQuestions[1]);

        questionRepository.removeQuestion(newQuestions[0]);

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
        var newQuestions = new Question[]{
                new Question("Why?", "Just because"),
                new Question("Why so serious?", "I'm so happy about it")
        };

        questionRepository.addQuestion(newQuestions[0]);
        questionRepository.addQuestion(newQuestions[1]);

        Assertions.assertEquals(newQuestions.length,
                questionRepository.getQuestionsAll().size());

        questionRepository.clear();

        Assertions.assertEquals(0, questionRepository.getQuestionsAll().size());
    }
}
