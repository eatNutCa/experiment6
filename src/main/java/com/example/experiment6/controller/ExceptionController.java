package com.example.experiment6.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    /**
     * 属性校验失败异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handleValidException(MethodArgumentNotValidException exception) {
        StringBuilder stringBuilder = new StringBuilder();
        exception.getBindingResult().getFieldErrors().forEach(e -> {
            stringBuilder.append(e.getField());
            stringBuilder.append(":");
            stringBuilder.append(e.getDefaultMessage());
            stringBuilder.append(";");
        });
        return Map.of("message", stringBuilder.toString());
    }

    /**
     * jackson JSON类型转换异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handleInvalidFormatException(InvalidFormatException exception) {
        StringBuilder stringBuilder = new StringBuilder();
        exception.getPath().forEach(p -> {
            stringBuilder.append("属性");
            stringBuilder.append(p.getFieldName());
            stringBuilder.append("，您输入的值：" + exception.getValue());
            stringBuilder.append("，类型错误！");
        });
        return Map.of("message", stringBuilder.toString());
    }

    /**
     * 方法及参数校验失败异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handleConstraintViolationException(ConstraintViolationException exception) {
        StringBuilder stringBuilder = new StringBuilder();
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        violations.forEach(v -> {
            stringBuilder.append(v.getMessage() + ";");
        });
        return Map.of("message", stringBuilder.toString());
    }
}
