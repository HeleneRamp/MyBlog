package org.wildcodeschool.myblog.exception;

public class ExceededMaxLengthException extends RuntimeException {
    public ExceededMaxLengthException(String message) {
        super(message);
    }
}
