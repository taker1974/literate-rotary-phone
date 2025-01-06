// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.model.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.BadQuestionException;
import org.skypro.exams.model.question.Question;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Хранилище объектов типа {@link Question}.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@Component
public final class QuestionRepository {

    // Вопросы брал откуда-то оттуда:
    //  https://habr.com/ru/articles/485678/
    //  https://github.com/enhorse/java-interview
    //  https://javarush.com/groups/posts/2590-top-50-java-core-voprosov-i-otvetov-na-sobesedovanii-chastjh-1

    @NotNull
    private final Collection<@NotNull Question> questions;

    private static final int INITIAL_CAPACITY = 100;

    /**
     * Конструктор по умолчанию.
     */
    public QuestionRepository() {
        this.questions = HashSet.newHashSet(INITIAL_CAPACITY);
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
     * Очистить коллекцию вопросов.
     */
    public void clear() {
        questions.clear();
    }

    /**
     * Загрузить вопросы из файла.
     * <p>
     * Файл должен находиться в директории статических ресурсов.<br>
     * Файл в исходном коде должен находиться в каталоге ресурсов<br>
     * <корень проекта>/src/main/resources/static и при сборке должен автоматически копироваться в<br>
     * target/classes/static/
     * </p>
     *
     * @param name имя файла в директории
     * @throws QuestionRepositoryException в случае ошибки работы с именем файла или с файлом
     */
    public void loadQuestionsFromTextFile(final String name)
            throws QuestionRepositoryException {

        final URI uri = getUri(name, false);

        /*  Файл должен находиться в директории статических ресурсов.
            Структура файла:

                # Комментарий 1
                // Комментарий 2
                Тест вопроса 1
                Строка 1 ответа 1
                ...
                Строка N ответа 1
                    <пустая строка>
                Тест вопроса 2
                Строка 1 ответа 2
                ...
                ...
                Тест вопроса N
                Строка 1 ответа N
                ...
                Строка N ответа N
                    <далее также возможны комментарии, пустые строки>
                EOF

            Читаем файл построчно, пропуская комментарии.
            Попутно делаем обрезку строк с пробелами.

            Непустую строку добавляем в StringBuilder + перевод строки.
            Если строка пустая, то вместо неё добавляем разделитель QUESTIONS_DELIMITER
            (просто для удобства в последующем split).

            Разбиваем строку из StringBuilder на строки-блоки, используя разделитель.

            После этого проходим по каждой строке-блоку, разбиваем такую строку по "\n",
            а затем первую строку каждого такого массива считаем вопросом,
            а все последующие строки - ответом.

            Создаем объект Question и добавляем его в коллекцию.
         */

        final var sb = new StringBuilder();
        try (final Stream<String> stream = Files.lines(Paths.get(uri))) {
            stream.filter(line -> !line.startsWith(COMMENT_1) && !line.startsWith(COMMENT_2))
                    .map(String::trim)
                    .forEach(s -> {
                        if (!s.isEmpty()) {
                            sb.append(s).append("\n");
                        } else {
                            sb.append(QUESTIONS_DELIMITER);
                        }
                    });
        } catch (Exception e) {
            throw new QuestionRepositoryException(e.getMessage());
        }

        final String[] chunks = sb.toString().split(QUESTIONS_DELIMITER);
        try {
            for (final String chunk : chunks) {
                final String[] lines = chunk.split("\n");
                if (lines.length >= 2) {
                    final String questionText = lines[0];

                    final StringBuilder innerSb = new StringBuilder();
                    for (int i = 1; i < lines.length; i++) {
                        innerSb.append(lines[i]).append("\n");
                    }
                    final String answerText = innerSb.toString();

                    final var question = new Question(questionText, answerText);
                    questions.add(question);
                }
            }
        } catch (BadQuestionException e) {
            throw new QuestionRepositoryException(e.getMessage());
        }
    }

    @NotNull
    private static URI getUri(String name, boolean isRoot)
            throws QuestionRepositoryException {

        if (name == null) {
            throw new QuestionRepositoryException("Имя текстового файла с вопросами не может быть null");
        }

        final String fileName = name.trim();
        if (fileName.isEmpty() && !isRoot) {
            throw new QuestionRepositoryException("Имя текстового файла с вопросами не может быть пустым");
        }

        final URI uri;
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            uri = Objects.requireNonNull(classloader.getResource(fileName)).toURI();
        } catch (Exception e) {
            throw new QuestionRepositoryException(e.getMessage());
        }
        return uri;
    }

    /**
     * Сохранить вопросы в файл json.
     * <p>
     * Сериализация Collection<{@link Question}> в формат json.
     * Файл будет находиться в директории статических ресурсов.
     * </p>
     *
     * @param name путь к файлу
     * @throws QuestionRepositoryException в случае ошибки работы с именем файла или с файлом
     */
    public void saveQuestionsToJson(final String name)
            throws QuestionRepositoryException {

        final URI uri = getUri("", true);

        try {
            final Path path = Paths.get(uri).resolve(name);
            final File file = new File(path.toString());

            // https://stackoverflow.com/questions/4105795/pretty-print-json-in-java

            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final String uglyJsonString = gson.toJson(questions);
            final JsonElement jsonElement = JsonParser.parseString(uglyJsonString);
            String prettyJsonString = gson.toJson(jsonElement);

            Files.createDirectories(path.getParent());

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(prettyJsonString);
            }
        } catch (Exception e) {
            throw new QuestionRepositoryException(e.getMessage());
        }
    }

    /**
     * Загрузить вопросы из файла json.
     * <p>
     * Структура json в данном случае - это сериализация Collection<{@link Question}>.<br>
     * Файл должен находиться в директории статических ресурсов.
     * </p>
     *
     * @param name имя файла в директории
     * @throws IllegalArgumentException если прочитанная из файла коллекция не может быть добавлена в хранилище
     */
    public void loadQuestionsFromJsonFile(String name)
            throws QuestionRepositoryException {

        final URI uri = getUri(name, false);

        try {
            final String path = uri.getPath();
            final File file = new File(path);

            // https://java2blog.com/gson-fromjson/
            // https://www.baeldung.com/gson-deserialization-guide

            final Gson gson = new Gson();
            Collection<Question> collection = fromJsonFile(gson, file);
            questions.addAll(collection);
        } catch (Exception e) {
            throw new QuestionRepositoryException(e.getMessage());
        }
    }

    private Collection<Question> fromJsonFile(Gson gson, File file)
            throws QuestionRepositoryException {

        Collection<Question> collection;
        try (var reader = new FileReader(file)) {
            collection = gson.fromJson(reader,
                    new TypeToken<Collection<Question>>() {
                    }.getType());

            if (collection == null) {
                throw new NullPointerException("Ошибка загрузки коллекции вопросов из файла json " +
                        file.getName() + ": коллекция не может быть null");
            }
        } catch (Exception e) {
            throw new QuestionRepositoryException(e.getMessage());
        }
        return collection;
    }

    /**
     * Добавить вопрос в хранилище.
     *
     * @param question вопрос
     */
    public void addQuestion(Question question)
            throws QuestionRepositoryException {

        if (question == null) {
            throw new QuestionRepositoryException("Ошибка добавления вопроса в хранилище: вопрос не может быть null");
        }
        questions.add(question);
    }

    /**
     * Удалить вопрос из хранилища.
     *
     * @param question вопрос
     */
    public void removeQuestion(Question question)
            throws QuestionRepositoryException {

        if (question == null) {
            throw new QuestionRepositoryException("Ошибка удаления вопроса из хранилища: вопрос не может быть null");
        }
        questions.remove(question);
    }

    /**
     * @return коллекция вопросов
     */
    @NotNull
    public Collection<Question> getQuestionsAll() {
        return Collections.unmodifiableCollection(questions);
    }
}
