package com.example.exception;

import com.example.dto.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.modelmapper.MappingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.naming.CommunicationException;
import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException( NoHandlerFoundException noHandlerFoundException, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "No handler found for " + noHandlerFoundException.getHttpMethod() + " " + noHandlerFoundException.getRequestURL();
        // ErrorDetails apiError = new ErrorDetails(ex.getLocalizedMessage(), error);
        ErrorDetails apiError = new ErrorDetails(error, "RA_SYS_ERR_404");
        log.info("handleNoHandlerFoundException : {} ",noHandlerFoundException.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler({CommunicationException.class, JDBCConnectionException.class,ConnectException.class, SocketTimeoutException.class})
    protected ResponseEntity<Object> handleNoHandlerFoundException( Exception exception) {
        ErrorDetails apiError = new ErrorDetails(exception.getMessage(), "RA_SYS_ERR_505");
        log.info("handleNoHandlerFoundException : {} ",exception.getMessage());
        return new ResponseEntity<Object>(apiError, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        ErrorDetails apiError = new ErrorDetails("validation_error","RA_SYS_ERR_487");
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("error_code","RA_SYS_ERR_487");
        mapping.put("controller_name", handlerMethod.getBeanType().getSimpleName());
        mapping.put("method_name", handlerMethod.getMethod().getName());
        mapping.put("message", "validation_error");
        mapping.put("HTTP_method", request.getMethod());
        mapping.put("url", request.getRequestURL().toString());
        log.error(mapping.toString());
        log.info(constraintViolationException.getMessage(),mapping.toString());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MappingException.class)
    public ResponseEntity<?> handleMappingException(MappingException mappingException) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        ErrorDetails apiError = new ErrorDetails(mappingException.getMessage(),"RA_SYS_ERR_505");
        //log.error("handleMethodArgumentTypeMismatch : {} ",methodArgumentTypeMismatchException.getMessage());
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("error_code","RA_SYS_ERR_505");
        mapping.put("controller_name", handlerMethod.getBeanType().getSimpleName());
        mapping.put("method_name", handlerMethod.getMethod().getName());
        mapping.put("message", mappingException.getMessage());
        mapping.put("HTTP_method", request.getMethod());
        mapping.put("url", request.getRequestURL().toString());
        log.error(mapping.toString());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(SQLException.class)
    public ResponseEntity<?> handleSQLException(SQLException sqlException) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        ErrorDetails apiError = new ErrorDetails(sqlException.getMessage(),"RA_SYS_ERR_505");
        //log.error("handleMethodArgumentTypeMismatch : {} ",methodArgumentTypeMismatchException.getMessage());
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("error_code","RA_SYS_ERR_505");
        mapping.put("controller_name", handlerMethod.getBeanType().getSimpleName());
        mapping.put("method_name", handlerMethod.getMethod().getName());
        mapping.put("message", sqlException.getMessage());
        mapping.put("HTTP_method", request.getMethod());
        mapping.put("url", request.getRequestURL().toString());
        log.error(mapping.toString());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException methodArgumentTypeMismatchException, WebRequest request) {
        HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HandlerMethod handlerMethod = (HandlerMethod) request1.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        String error = methodArgumentTypeMismatchException.getName() + " should be of type " + methodArgumentTypeMismatchException.getRequiredType().getName();
        ErrorDetails apiError = new ErrorDetails(error,"RA_SYS_ERR_404");
        //log.error("handleMethodArgumentTypeMismatch : {} ",methodArgumentTypeMismatchException.getMessage());
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("error_code","RA_SYS_ERR_400");
        mapping.put("controller_name", handlerMethod.getBeanType().getSimpleName());
        mapping.put("method_name", handlerMethod.getMethod().getName());
        mapping.put("message", error);
        mapping.put("HTTP_method", request1.getMethod());
        mapping.put("url", request1.getRequestURL().toString());
        log.error(mapping.toString());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        Map<String, String> errorsMap = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach((error) -> {
            errorsMap.put(error.getField(), error.getDefaultMessage());
        });
        ErrorDetails errorDetails = new ErrorDetails("method argument not valid", "RA_SYS_ERR_400");
        //log.error("handleMethodArgumentNotValid : {} ",methodArgumentNotValidException.getMessage());
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("error_code","RA_SYS_ERR_400");
        mapping.put("controller_name", handlerMethod.getBeanType().getSimpleName());
        mapping.put("method_name", handlerMethod.getMethod().getName());
        mapping.put("message", "method argument not valid");
        mapping.put("HTTP_method", request.getMethod());
        mapping.put("url", request.getRequestURL().toString());
       // mapping.put("data", String.valueOf(errorsMap));
        log.error(mapping.toString());
        log.info(errorDetails.toString(),mapping.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        ErrorDetails errorDetails = new ErrorDetails("Json Parse Error", "RA_FW_ERR_484");
        //log.error("handleProductNotFoundException : {} ",httpMessageNotReadableException.getMessage());
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("error_code","RA_FW_ERR_484");
        mapping.put("controller_name", handlerMethod.getBeanType().getSimpleName());
        mapping.put("method_name", handlerMethod.getMethod().getName());
        mapping.put("message", "Json Parse Error");
        mapping.put("HTTP_method", request.getMethod());
        mapping.put("url", request.getRequestURL().toString());
        log.error(mapping.toString());
        log.info(httpMessageNotReadableException.getMessage(),mapping.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleProductNotFoundException(ProductNotFoundException productNotFoundException) {
        ErrorDetails errorDetails = new ErrorDetails(productNotFoundException.getMessage(), "RA_SYS_ERR_404");
        log.info("handleProductNotFoundException : {} ",productNotFoundException.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        ErrorDetails errorResponse = new ErrorDetails(illegalArgumentException.getMessage(), "RA_SYS_ERR_500");
        log.info("handleIllegalArgumentException:{}", illegalArgumentException.getMessage());
        return new ResponseEntity<ErrorDetails>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorDetails> handleRuntimeException(RuntimeException runtimeException) {
        ErrorDetails errorDetails = new ErrorDetails(runtimeException.getMessage(), "RA_SYS_ERR_500");
        log.info("handleRuntimeException :{} ",runtimeException.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleException(Exception exception) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(), "RA_SYS_ERR_500");
        log.info("handleException : {} ",exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}






























    /*@org.springframework.web.bind.annotation.ExceptionHandler({NumberFormatException.class})
    public ResponseEntity<ErrorDetails> handleNumberFormatException(NumberFormatException numberFormatException) {
        ErrorDetails errorDetails = new ErrorDetails("Invalid format", "RESTAPI_ERR_04");
        log.error(numberFormatException.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }*/