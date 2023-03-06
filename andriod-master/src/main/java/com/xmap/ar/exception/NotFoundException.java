package com.xmap.ar.exception;

public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(String.format("%s not found.", message));
    }

    public NotFoundException(Class<?> clazz, int id) {
        super(String.format("Entity %s with id %d not found.", clazz.getSimpleName(), id));
    }

}