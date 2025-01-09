// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.tools;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
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
     * Проверить, существует ли ресурсный файл.
     *
     * @param filename имя файла
     * @return true, если файл существует
     */
    public static boolean isResourceFileExists(final String filename) {
        if (filename == null) {
            return false;
        }

        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            final URI uri = Objects.requireNonNull(classloader.getResource(filename)).toURI();

            final String path = uri.getPath();
            final File file = new File(path);

            return file.exists();
        } catch (Exception e) {
            return false;
        }
    }
}
