package dev.sameer.ecommerceuserservice.Exception;

import dev.sameer.ecommerceuserservice.DTO.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handlesUserNotFound(UserNotFoundException userNotFoundException) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                userNotFoundException.getMessage(),
                404
        );
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredential.class)
    public ResponseEntity<ExceptionResponseDTO> handlesUnAuthorizedUser(InvalidCredential invalidCredential) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                invalidCredential.getMessage(),
                401
        );
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponseDTO> handlesInvalidToken(InvalidTokenException invalidTokenException) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                invalidTokenException.getMessage(),
                498
        );
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.NOT_FOUND);
    }
}
