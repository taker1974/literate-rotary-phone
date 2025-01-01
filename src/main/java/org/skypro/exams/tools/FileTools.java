// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.tools;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

/**
 * Инструменты работы с файлами.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public final class FileTools {

    private FileTools() {
        throw new UnsupportedOperationException();
    }

    // https://www.delftstack.com/howto/java/how-to-get-the-current-working-directory-in-java/

    /**
     * Получить путь к папке с классом.
     *
     * @param thisClass класс
     * @return путь к папке с классом
     * @throws IllegalArgumentException если класс не найден
     */
    @NotNull
    public static String getClassDirectory(@NotNull Class<?> thisClass) {
        final String className = thisClass.getSimpleName() + ".class";
        final URL url = thisClass.getResource(className);
        if (url == null) {
            throw new IllegalArgumentException("Не удалось получить путь к файлу " + className);
        }

        final File file = new File(url.getFile());
        return file.getParent();
    }

    /**
     * Проверить, существует ли ресурсный файл.
     *
     * @param filename имя файла
     * @return true, если файл существует
     * @throws URISyntaxException если не удалось получить путь к файлу
     */
    public static boolean isResourceFileExists(final String filename)
            throws URISyntaxException {
        if (filename == null) {
            return false;
        }

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final URI uri = Objects.requireNonNull(classloader.getResource(filename)).toURI();

        final String path = uri.getPath();
        final File file = new File(path);

        return file.exists();
    }
}
