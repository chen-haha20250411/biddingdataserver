package com.xiao.exception;

import com.xiao.base.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class SqlException {
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(SQLException.class)
	public ResultModel sqlException(SQLException e) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(baos));
		String exception = baos.toString();
		log.info(exception);

		if (e instanceof SQLIntegrityConstraintViolationException) {
			return new ResultModel(false,"该数据有关联数据，操作失败!");
		}
		return new ResultModel(false,"数据库异常，操作失败!");
	}
}
