package org.wildcodeschool.myblog.exception;

import java.nio.file.AccessDeniedException;

public class ArticleAccessDeniedException extends AccessDeniedException {
    public ArticleAccessDeniedException(String message) {
        super(message);
    }
}
