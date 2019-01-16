package com.leigq.www.web;

import java.util.Set;

import javax.security.auth.login.LoginException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import com.leigq.www.bean.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**
 * 通用异常处理器，用于处理所有开发者没有手动捕获的异常情况，自动记录到日志文件，并发送异常 Json 信息到前台。
 * <br><br>
 * 创建人：袁炜林 <br>
 * 创建时间：2017年10月29日 下午12:51:33 <br>
 * 修改人： <br>
 * 修改时间： <br>
 * 修改备注： <br>
 * 
 * @version V1.0
 */
@EnableWebMvc // 最好加上这句
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /*
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String msg = "缺少请求参数！";
        log.error(msg, e);
        return new Response().failure(HttpStatus.BAD_REQUEST.toString(), msg, null);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String msg = "参数解析失败：" + e.getMessage();
        log.error(msg, e);
        return new Response().failure(HttpStatus.BAD_REQUEST.toString(), msg, null);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder("参数验证失败！\n");
		handleBindingResult(e.getBindingResult(), sb);
		String msg = sb.toString();
        log.error(msg, e);
        return new Response().failure(HttpStatus.BAD_REQUEST.toString(), msg, null);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Response handleBindException(BindException e) {
        StringBuilder sb = new StringBuilder("参数绑定失败！");
		handleBindingResult(e.getBindingResult(), sb);
		String msg = sb.toString();
        log.error(msg, e);
        return new Response().failure(HttpStatus.BAD_REQUEST.toString(), msg, null);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Response handleServiceException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder("参数验证失败！\n");
		
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		for (ConstraintViolation<?> violation : violations) {
			sb.append(violation.getMessage());
		}
		
    	String msg = sb.toString();
        log.error(msg, e);
        return new Response().failure(HttpStatus.BAD_REQUEST.toString(), msg, null);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Response handleValidationException(ValidationException e) {
    	String msg = "参数验证失败：" + e.getMessage();
        log.error(msg, e);
        return new Response().failure(HttpStatus.BAD_REQUEST.toString(), msg, null);
    }

    /**
     * 401 - Unauthorized
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginException.class)
    public Response handleLoginException(LoginException e) {
    	String msg = e.getMessage();
        log.error(msg, e);
        return new Response().failure(HttpStatus.UNAUTHORIZED.toString(), msg, null);
    }

    /**
     * 403 - Forbidden
     */
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ExceptionHandler(PermissionException.class)
//    public Response handlePermissionException(PermissionException e) {
//    	String msg = e.getMessage();
//        log.error(msg, e);
//        return new Response().failure(HttpStatus.FORBIDDEN.toString(), msg, null);
//    }
    
    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    	String msg = "不支持当前请求方法！";
        log.error(msg, e);
        return new Response().failure(HttpStatus.METHOD_NOT_ALLOWED.toString(), msg, null);
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Response handleHttpMediaTypeNotSupportedException(Exception e) {
    	String msg = "不支持当前媒体类型！";
        log.error(msg, e);
        return new Response().failure(HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString(), msg, null);
    }

    /**
     * 422 - UNPROCESSABLE_ENTITY
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Response handleMaxUploadSizeExceededException(Exception e) {
    	String msg = "所上传文件大小超过最大限制，上传失败！";
        log.error(msg, e);
        return new Response().failure(HttpStatus.UNPROCESSABLE_ENTITY.toString(), msg, null);
    }
    
    /**
     * 500 - Internal Server Error
     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(ServiceException.class)
//    public Response handleServiceException(ServiceException e) {
//    	String msg = "服务内部异常：" + e.getMessage();
//        log.error(msg, e);
//        return new Response().failure(HttpStatus.INTERNAL_SERVER_ERROR.toString(), msg, null);
//    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
    	String msg = "服务内部异常！";
        log.error(msg, e);
        return new Response().failure(HttpStatus.INTERNAL_SERVER_ERROR.toString(), msg, null);
    }
    

	/**
	 * 处理参数绑定异常，并拼接出错的参数异常信息。
	 * <p>
	 * 创建人：袁炜林 <br>
	 * 创建时间：2017年10月16日 下午9:09:22 <br>
	 * <p>
	 * 修改人： <br>
	 * 修改时间： <br>
	 * 修改备注： <br>
	 * </p>
	 * @param e 异常对象
	 * @param msg 异常消息
	 */
	private void handleBindingResult(BindingResult result, StringBuilder msg) {
		if (result.hasErrors()) {
			result.getAllErrors().forEach(err -> {
				FieldError fieldError = (FieldError) err;
				// 错误的 属性名 ： 属性值
				msg.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append("\n");
			});
		}
	}
	
}
