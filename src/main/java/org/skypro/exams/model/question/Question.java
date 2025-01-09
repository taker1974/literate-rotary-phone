// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.model.question;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.tools.StringTools;

import java.util.Objects;

/**
 * Вопрос и ответ.<br>
 * Хранит вопрос и ответ на него.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@SuppressWarnings("java:S6206") // class -> record
public final class Question {
    /**
     * Минимальная длина текста вопроса.
     */
    public static final int MIN_QUESTION_LENGTH = 3;

    /**
     * Максимальная длина текста вопроса.
     */
    public static final int MAX_QUESTION_LENGTH = 1_000;

    /**
     * Минимальная длина текста ответа.
     */
    public static final int MIN_ANSWER_LENGTH = 1;

    /**
     * Максимальная длина текста ответа.
     */
    public static final int MAX_ANSWER_LENGTH = 10_000;

    @NotNull
    private final String questionText;

    @NotNull
    public String getQuestionText() {
        return questionText;
    }

    @NotNull
    private final String answerText;

    @NotNull
    public String getAnswerText() {
        return answerText;
    }

    /**
     * Конструктор.<br>
     * Создает вопрос и ответ.
     *
     * @param questionText текст вопроса
     * @param answerText   текст ответа
     */
    public Question(@NotNull String questionText, @NotNull String answerText)
            throws BadQuestionException {

        if (StringTools.isBadString(questionText, MIN_QUESTION_LENGTH, MAX_QUESTION_LENGTH)) {
            throw new BadQuestionException(
                    String.format("Вопрос не должен быть null и его длина должна быть от %d до %d символов",
                            MIN_QUESTION_LENGTH, MAX_QUESTION_LENGTH));
        }

        if (StringTools.isBadString(answerText, MIN_ANSWER_LENGTH, MAX_ANSWER_LENGTH)) {
            throw new BadQuestionException(
                    String.format("Ответ не должен быть null и его длина должна быть от %d до %d символов",
                            MIN_ANSWER_LENGTH, MAX_ANSWER_LENGTH));
        }

        this.questionText = questionText;
        this.answerText = answerText;
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionText, answerText);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question that)) {
            return false;
        }
        return Objects.equals(getQuestionText(), that.getQuestionText()) &&
                Objects.equals(getAnswerText(), that.getAnswerText());
    }

    @Override
    public String toString() {
        return String.format("Вопрос: %s%nОтвет: %s", getQuestionText(), getAnswerText());
    }
}
