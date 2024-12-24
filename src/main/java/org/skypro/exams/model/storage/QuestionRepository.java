// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.model.storage;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.tools.FileTools;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;

/**
 * Хранилище объектов типа {@link org.skypro.exams.model.question.Question}.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@Log4j2
public class QuestionRepository {

    // https://habr.com/ru/articles/485678/
    // https://github.com/enhorse/java-interview
    // https://javarush.com/groups/posts/2590-top-50-java-core-voprosov-i-otvetov-na-sobesedovanii-chastjh-1

    @NotNull
    private final Collection<Question> questions;

    private static final int INITIAL_CAPACITY = 100;

    /**
     * Конструктор по умолчанию.
     */
    public QuestionRepository() {
        this.questions = HashSet.newHashSet(INITIAL_CAPACITY);
    }

    /**
     * Конструктор с параметрами.
     *
     * @param questions коллекция вопросов
     */
    @Autowired
    public QuestionRepository(@NotNull Collection<Question> questions) {
        this.questions = questions;
    }

    public void loadQuestionsFromTextFile(@NotNull String name) throws IOException {
        // формируем путь к файлу
        var path = FileTools.getClassDirectory(this.getClass());
        var file = new File(path, name);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл " + file.getName() + " не найден");
        }

        // читаем весь файл и разбиваем его на блоки
        var sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(file.getName()))) {
            stream.filter(line -> !line.startsWith("#") && !line.startsWith("//"))
                    .map(String::trim)
                    .forEach(s -> sb.append(s).append("\n"));
        } catch (IOException e) {
            throw new IOException(e);
        }

        String allLines = sb.toString();
        var chunks = allLines.split("\n");

        int i = chunks.length;
    }

    /**
     * Загрузить вопросы из файла.
     *
     * @param name имя файла в директории
     * @throws FileNotFoundException если файл не найден
     */
    public void loadQuestions(@NotNull String name) throws FileNotFoundException {
        // формируем путь к файлу
        var path = FileTools.getClassDirectory(this.getClass());
        var file = new File(path, name);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл " + file.getName() + " не найден");
        }

        // TODO: загрузить вопросы из файла
    }

    /**
     * Сохранить вопросы в файл.
     *
     * @param name путь к файлу
     */
    public void saveQuestions(@NotNull final String name) throws IOException {
        // формируем путь к файлу
        final var path = FileTools.getClassDirectory(this.getClass());
        final var file = new File(path, name);

        final var gson = new Gson();
        final String json = gson.toJson(questions);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    /**
     * Добавить вопрос в хранилище.
     *
     * @param question вопрос
     */
    public void addQuestion(@NotNull Question question) {
        questions.add(question);
    }

    /**
     * Удалить вопрос из хранилища.
     *
     * @param question вопрос
     */
    public Question removeQuestion(@NotNull Question question) {
        questions.remove(question);
        return question;
    }

    /**
     * @return коллекция вопросов
     */
    @NotNull
    public Collection<Question> getQuestionsAll() {
        return Collections.unmodifiableCollection(questions);
    }
}
