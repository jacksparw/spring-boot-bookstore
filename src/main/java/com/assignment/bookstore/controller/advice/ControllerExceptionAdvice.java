package com.assignment.bookstore.controller.advice;

import com.assignment.bookstore.beans.dto.GenericResponseDTO;
import com.assignment.bookstore.exception.AppDatabaseException;
import com.assignment.bookstore.exception.NoDataFoundException;
import com.assignment.bookstore.exception.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.GENERIC_ERROR_MESSAGE;
import static com.assignment.bookstore.util.MessageConstants.ErrorMessage.INVALID_MESSAGE;
import static com.assignment.bookstore.util.MessageConstants.RequestStatus.STATUS_FAILURE;

@Log4j2
@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<GenericResponseDTO> validationException(Exception ex) {
        log.error(ex.getMessage());

        GenericResponseDTO response = new GenericResponseDTO();
        response.setMessage(ex.getMessage());
        response.setStatus(STATUS_FAILURE);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<GenericResponseDTO> handleHttpMessageNotReadableException() {
        GenericResponseDTO response = new GenericResponseDTO();
        response.setMessage(INVALID_MESSAGE);
        response.setStatus(STATUS_FAILURE);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppDatabaseException.class)
    public ResponseEntity<GenericResponseDTO> handleAppDatabaseException(Exception ex) {

        log.error(ex.getMessage(), ex);

        GenericResponseDTO response = new GenericResponseDTO();
        response.setMessage(ex.getMessage());
        response.setStatus(STATUS_FAILURE);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<GenericResponseDTO> handleNoResultException(Exception ex) {

        log.info(ex.getMessage());

        GenericResponseDTO response = new GenericResponseDTO();
        response.setMessage(ex.getMessage());
        response.setStatus(STATUS_FAILURE);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponseDTO> handleException(Exception ex) {

        log.error(ex.getMessage(), ex);

        GenericResponseDTO response = new GenericResponseDTO();
        response.setMessage(GENERIC_ERROR_MESSAGE);
        response.setStatus(STATUS_FAILURE);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
