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
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Хранилище объектов типа {@link org.skypro.exams.model.question.Question}.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@Log4j2
public class QuestionRepository {

    // Вопросы брал откуда-то оттуда:
    //  https://habr.com/ru/articles/485678/
    //  https://github.com/enhorse/java-interview
    //  https://javarush.com/groups/posts/2590-top-50-java-core-voprosov-i-otvetov-na-sobesedovanii-chastjh-1

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

    /**
     * Внутренний разделитель блоков с вопросами и ответами.
     * Нужен только для разбора файла.
     */
    public static final String QUESTIONS_DELIMITER = "Q:\n";

    /**
     * Комментарий в файле вопроса, вариант 1.
     */
    public static final String COMMENT_1 = "#";

    /**
     * Комментарий в файле вопроса, вариант 2.
     */
    public static final String COMMENT_2 = "//";

    /**
     * Загрузить вопросы из файла.
     *
     * @param name имя файла в директории
     * @throws IOException        если файл не найден
     * @throws URISyntaxException если файл не найден
     */
    public void loadQuestionsFromTextFile(@NotNull String name) throws IOException, URISyntaxException {
        /* Файл должен находиться в директории статических ресурсов.
            Структура файла:

                # Комментарий 1
                // Комментарий 2
                Тест вопроса 1
                Ответ 1
                    <пустая строка>
                Тест вопроса 2
                Ответ 2
                ...
                Тест вопроса N
                Ответ N

            Читаем файл построчно, пропуская комментарии.
            Попутно делаем обрезку строк с пробелами.

            Непустую строку добавляем в StringBuilder.
            Если строка пустая, то вместо неё добавляем разделитель QUESTIONS_DELIMITER
            (просто для удобства в последующем split).

            Разбиваем строку из StringBuilder на строки-блоки, используя разделитель.

            После этого проходим по каждой строке-блоку, разбиваем такую строку по "\n",
            а затем первую строку каждого такого массива считаем вопросом,
            а все последующие строки - ответом.

            Создаем объект Question и добавляем его в коллекцию.
         */
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var uri = Objects.requireNonNull(classloader.getResource(name)).toURI();

        // читаем весь файл и разбиваем его на блоки
        // если строка начинается с # или //, то пропускаем ее
        // если строка пустая, то заменяем её на Q:
        var sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(uri))) {
            stream.filter(line -> !line.startsWith(COMMENT_1) && !line.startsWith(COMMENT_2))
                    .map(String::trim)
                    .forEach(s -> {
                        if (!s.isEmpty()) {
                            sb.append(s).append("\n");
                        } else {
                            sb.append(QUESTIONS_DELIMITER);
                        }
                    });
        }

        var chunks = sb.toString().split(QUESTIONS_DELIMITER);
        Stream.of(chunks).
                forEach(chunk -> {
                    var lines = chunk.split("\n");
                    if (lines.length >= 2) {
                        String questionText = lines[0];

                        var innerSb = new StringBuilder();
                        for (int i = 1; i < lines.length; i++) {
                            innerSb.append(lines[i]).append("\n");
                        }
                        String answerText = innerSb.toString();

                        var question = new Question(questionText, answerText);
                        questions.add(question);
                    }
                });
    }

    /**
     * Загрузить вопросы из файла json.
     *
     * @param name имя файла в директории
     * @throws FileNotFoundException если файл не найден
     */
    public void loadQuestionsFromJson(@NotNull String name) throws FileNotFoundException {
        var path = FileTools.getClassDirectory(this.getClass());
        var file = new File(path, name);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл " + file.getName() + " не найден");
        }

        // TODO: загрузить вопросы из файла json
    }

    /**
     * Сохранить вопросы в файл json.
     *
     * @param name путь к файлу
     */
    public void saveQuestionsToJson(@NotNull final String name) throws IOException {
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
