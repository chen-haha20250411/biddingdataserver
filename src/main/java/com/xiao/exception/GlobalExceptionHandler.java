package com.xiao.exception;

import com.xiao.base.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器 - 兼容 vue-element-admin 格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultModel handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.error("运行时异常: {} - {}", request.getRequestURI(), ex.getMessage(), ex);
        return ResultModel.error("系统异常，操作失败!");
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultModel handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        log.error("空指针异常: {} - {}", request.getRequestURI(), ex.getMessage(), ex);
        return ResultModel.error("系统异常，空指针错误!");
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultModel handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("参数验证异常: {} - {}", request.getRequestURI(), ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResultModel.error(40000, "参数验证失败", errors);
    }

    /**
     * 处理数据绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultModel handleBindException(BindException ex, HttpServletRequest request) {
        log.error("数据绑定异常: {} - {}", request.getRequestURI(), ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResultModel.error(40000, "数据绑定失败", errors);
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultModel handleException(Exception ex, HttpServletRequest request) {
        log.error("系统异常: {} - {}", request.getRequestURI(), ex.getMessage(), ex);
        return ResultModel.error("系统异常，请稍后重试!");
    }
}