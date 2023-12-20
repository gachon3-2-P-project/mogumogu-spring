package com.mogumogu.spring.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BusinessLogicException extends RuntimeException {

//    @Getter
//    private ExceptionCode exceptionCode;
//
//    public BusinessLogicException(ExceptionCode exceptionCode){
//        super(exceptionCode.getMessage());
//        this.exceptionCode = exceptionCode;
//    }
//
//    public ExceptionCode getExceptionCode() { return exceptionCode; }
//
//    public HttpStatus getHttpStatus() {
//        return httpStatus;
//    }

    private final HttpStatus httpStatus;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.httpStatus = exceptionCode.getHttpStatus();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
