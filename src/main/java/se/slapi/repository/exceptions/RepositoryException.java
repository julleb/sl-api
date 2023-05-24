package se.slapi.repository.exceptions;

public class RepositoryException extends Exception {
    public RepositoryException(String message, Exception e) {
        super(message, e);
    }

    public RepositoryException(String message) {
        super(message);
    }
}
