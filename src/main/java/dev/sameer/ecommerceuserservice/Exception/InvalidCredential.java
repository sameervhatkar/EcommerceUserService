package dev.sameer.ecommerceuserservice.Exception;

public class InvalidCredential extends RuntimeException {

    public InvalidCredential(String message) {
        super(message);
    }
}
