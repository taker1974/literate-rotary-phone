// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams;

import org.jetbrains.annotations.NotNull;
import org.skypro.exams.model.question.BadQuestionException;
import org.skypro.exams.model.question.Question;

import java.util.Collection;
import java.util.UUID;

/**
 * ExamsTestTools.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.2
 */
public final class ExamsTestTools {

    private ExamsTestTools() {
        // ...
    }

    public static Question[] getSomeQuestions() throws BadQuestionException {
        return new Question[]{
                new Question("What can I do?",
                        "Some people want to see you fail. Disappoint them!"),
                new Question("Why so serious?",
                        "Let`s put a smile on that face!"),
                new Question("Док, а как же все эти разговоры, что нельзя менять будущее? " +
                        "Про пространственно-временной континуум.",
                        "Да я подумал, ну его к черту, этот континуум"),
                new Question("А жизнь — всегда такое дерьмо или только в детстве?",
                        "Всегда"),
                new Question("Вам дать мою визитку?",
                        "Нет, не надо! Я ведь детектив, я вас сам найду!"),
        };
    }

    public static Question getAt(final Collection<Question> questions, final int index) {
        if (index >= 0 && index < questions.size()) {
            int count = index;
            for (Question question : questions) {
                if (count <= 0) {
                    return question;
                }
                count--;
            }
        }
        return null;
    }

    public static Question[] getPredictableQuestions(final int amount, @NotNull final String prefix)
            throws BadQuestionException {

        var questions = new Question[amount];
        for (int i = 0; i < amount; i++) {
            var question = new Question(
                    String.format("%s-%d", prefix, i),
                    String.format("%s-%s", prefix, UUID.randomUUID()));
            questions[i] = question;
        }
        return questions;
    }
}
