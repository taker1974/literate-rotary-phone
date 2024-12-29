// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.java;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.model.storage.QuestionRepository;
import org.skypro.exams.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Random;

/**
 * Сервис для работы с вопросами по Java.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@Service
public final class JavaQuestionService implements QuestionService {

    @NotNull
    private final QuestionRepository questionRepository;

    @NotNull
    private final Random random;

    /**
     * Конструктор.
     *
     * @param questionRepository репозиторий вопросов
     */
    public JavaQuestionService(@NotNull QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        random = new Random();
    }

    @Override
    public Collection<Question> getQuestionsAll() {
        return questionRepository.getQuestionsAll();
    }

    @Override
    public Question addQuestion(Question question) {
        questionRepository.addQuestion(question);
        return question;
    }

    @Override
    public Question addQuestion(String questionText, String answerText) {
        Question question = new Question(questionText, answerText);
        questionRepository.addQuestion(question);
        return question;
    }

    @Override
    public Question removeQuestion(Question question) {
        questionRepository.removeQuestion(question);
        return question;
    }

    @Override
    public Question getRandomQuestion() {
        int count = random.nextInt(1, questionRepository.getQuestionsAll().size());
        for (Question question : questionRepository.getQuestionsAll()) {
            if (--count == 0) {
                return question;
            }
        }
        throw new IllegalStateException("Не удалось найти вопрос");
    }
}
