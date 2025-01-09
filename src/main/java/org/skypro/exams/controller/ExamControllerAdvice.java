// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.controller;

import org.skypro.exams.service.examiner.ExaminerServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Обработка ошибок контроллера ExamController.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@ControllerAdvice
@SuppressWarnings("unused") // ошибочное определение объекта кода, как неиспользуемого
public class ExamControllerAdvice {

    @ExceptionHandler(ExaminerServiceException.class)
    public ResponseEntity<ExamError> handleTooManyQuestionsException(ExaminerServiceException e) {
        var error = new ExamError("0x0002h", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
