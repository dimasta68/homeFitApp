package com.hardcoding.homework.Interface;

public interface Validator<T> {

    boolean isValid(T value);

    String getDescription();
}