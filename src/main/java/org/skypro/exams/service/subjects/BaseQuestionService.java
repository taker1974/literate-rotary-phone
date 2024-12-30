// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.service.subjects;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.Question;
import org.skypro.exams.model.storage.QuestionRepository;
import org.skypro.exams.tools.FileTools;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Random;

/**
 * Базовый класс для сервисов работы с вопросами.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
public abstract class BaseQuestionService implements QuestionService {

    @NotNull
    protected final QuestionRepository questionRepository;

    @NotNull
    protected final Random random;

    /**
     * Конструктор.
     *
     * @param questionRepository репозиторий вопросов
     */
    protected BaseQuestionService(@NotNull QuestionRepository questionRepository,
                                  final String jsonFilename, final String textFilename)
            throws URISyntaxException, IOException {

        random = new Random();

        this.questionRepository = questionRepository;
        if (FileTools.isResourceFileExists(jsonFilename)) {
            this.questionRepository.loadQuestionsFromJsonFile(jsonFilename);
        } else if (FileTools.isResourceFileExists(textFilename)) {
            this.questionRepository.loadQuestionsFromTextFile(textFilename);
        } else {
            throw new IllegalStateException("Не удалось найти файл с вопросами");
        }
    }

    /**
     * @return количество вопросов в хранилище
     */
    @Override
    public int getAmountOfQuestions() {
        return questionRepository.getQuestionsAll().size();
    }

    @Override
    public Collection<Question> getQuestionsAll() {
        return questionRepository.getQuestionsAll();
    }

    @Override
    public void addQuestion(Question question) {
        questionRepository.addQuestion(question);
    }

    @Override
    public void addQuestion(String questionText, String answerText) {
        Question question = new Question(questionText, answerText);
        addQuestion(question);
    }

    @Override
    public void removeQuestion(Question question) {
        questionRepository.removeQuestion(question);
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
