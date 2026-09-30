package com.example.orderledger.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ExceptionResponse handleBusinessException(BusinessException ex, HttpServletResponse response) {
        response.setStatus(ex.getExceptionConstants().getStatusCode());
        return new ExceptionResponse(ex.getExceptionConstants().getMessage(), ex.getExceptionConstants().getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleGeneralException(Exception ex, HttpServletResponse response) {
        response.setStatus(500);
        return new ExceptionResponse("Daxili server xətası: " + ex.getMessage(), 500);
    }
}
