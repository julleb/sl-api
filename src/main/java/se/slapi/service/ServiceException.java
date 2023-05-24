package se.slapi.service;

public class ServiceException extends Exception {
    public ServiceException(String message, Exception e) {
        super(message, e);
    }
}
