// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.model.storage.QuestionRepository;
import org.skypro.exams.service.java.JavaQuestionService;

import java.util.Random;

@ExtendWith(MockitoExtension.class)
class JavaQuestionServiceTest {

    @Mock
    private final Random random;

    @Mock
    private final JavaQuestionService service;

    @Spy
    private final QuestionRepository questionRepository;

    public JavaQuestionServiceTest() {
        this.random = new Random();
        this.questionRepository = new QuestionRepository();
        this.service = new JavaQuestionService(questionRepository);
    }

    @Test
    void whenGetAmountOfQuestions_thenReturnAmountOfQuestions() {
        questionRepository.clear();
        Assertions.assertEquals(0, service.getAmountOfQuestions());

        // добавляем пару вопросов вручную
        var newQuestions = new Question[]{
                new Question("Why?", "Just because"),
                new Question("Why so serious?", "I'm so happy about it")
        };

        questionRepository.addQuestion(newQuestions[0]);
        questionRepository.addQuestion(newQuestions[1]);

        Assertions.assertEquals(questionRepository.getQuestionsAll().size(), service.getAmountOfQuestions());
    }

    @Test
    void whenGetRandomQuestion_thenReturnRandomQuestion() {
        questionRepository.clear();

        // добавляем несколько вопросов вручную
        var newQuestions = new Question[]{
                new Question("Why?", "Just because"),
                new Question("Why so serious?", "I'm so happy about it"),
                new Question("So, did you tell some?", "Maybe-baby"),
                new Question("Who are you?", "I'm a programmer"),
                new Question("How old are you?", "I saw many things, but I don't know what they are"),
                new Question("Did you have anything?", "Yes, I have a few things"),
        };
        for (var question : newQuestions) {
            questionRepository.addQuestion(question);
        }

        int min = 1;
        int max = questionRepository.getQuestionsAll().size();

        Mockito.when(random.nextInt(min, max)).thenReturn(min);
        Assertions.assertEquals(service.getRandomQuestion(), newQuestions[min - 1]);

        Mockito.when(random.nextInt(min, max)).thenReturn(max);
        Assertions.assertEquals(service.getRandomQuestion(), newQuestions[max - 1]);

        Mockito.when(random.nextInt(min, max)).thenReturn(max / 2);
        Assertions.assertEquals(service.getRandomQuestion(), newQuestions[max / 2 - 1]);
    }
}
