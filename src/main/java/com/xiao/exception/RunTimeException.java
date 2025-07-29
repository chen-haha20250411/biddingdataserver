package com.xiao.exception;

import com.xiao.base.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@RestControllerAdvice
public class RunTimeException {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(RuntimeException.class)
    public ResultModel runtimeException(RuntimeException ex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(baos));
        log.info(baos.toString());
        return new ResultModel(false, "系统异常，操作失败!");
    }
}
