// SkyPro
// Терских Константин, kostus.online.1974@yandex.ru, 2024
// Курсовая работа. Java Core.

package org.skypro.exams.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.MethodNotAllowedException;

/**
 * Обработка ошибок контроллера ArchController.
 *
 * @author Константин Терских, kostus.online.1974@yandex.ru, 2024
 * @version 1.1
 */
@ControllerAdvice
public class ArchControllerAdvice {

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ExamError> handleMethodNotAllowedException(MethodNotAllowedException e) {
        var error = new ExamError("0x0003h", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
