package com.cherry.youliao.utils;

import org.springframework.dao.DuplicateKeyException;

public class ExceptionUtils {

    public static boolean hasDuplicateEntryException(Exception e) {

        if (e instanceof DuplicateKeyException) {
            return true;
        }

        if (e.getMessage().contains("Duplicate entry")) {
            return true;
        }

        Throwable cause = e.getCause();

        if (cause != null && cause.getMessage().contains("Duplicate entry")) {
            return true;
        }

        return false;
    }

}
