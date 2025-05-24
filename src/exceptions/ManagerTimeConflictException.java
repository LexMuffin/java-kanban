package exceptions;

public class ManagerTimeConflictException extends RuntimeException{
    public ManagerTimeConflictException(String message) {
        super(message);
    }
}

