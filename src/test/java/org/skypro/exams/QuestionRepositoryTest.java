// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.exams.model.storage.QuestionRepository;

/**
 * QuestionRepositoryTest.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@ExtendWith(MockitoExtension.class)
class QuestionRepositoryTest {

    @Test
    void whenLoadQuestionFromTextFile_thenQuestionsAreLoaded() {
        QuestionRepository questionRepository = new QuestionRepository();

        Assertions.assertThrows(RuntimeException.class, () ->
                questionRepository.loadQuestionsFromTextFile(null));
        Assertions.assertThrows(RuntimeException.class, () ->
                questionRepository.loadQuestionsFromTextFile(""));
        Assertions.assertThrows(RuntimeException.class, () ->
                questionRepository.loadQuestionsFromTextFile("bad.file.name"));

//        final String goodFileName = "static/Questions-Java-Core.txt";
//        questionRepository.loadQuestionsFromTextFile(goodFileName);
//        Assertions.assertEquals(100, questionRepository.getQuestions().size());
    }
}
