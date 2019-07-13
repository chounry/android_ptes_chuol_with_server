package com.group6.choul.login_register_handling;

import java.util.List;
import java.util.Map;

public class ApiError {
    String message;
    Map<String, List<String>> errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }
}
