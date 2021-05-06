package com.hardcoding.homework.Interface;

import android.text.TextUtils;

public class EmptyValidator implements Validator<String> {

    @Override
    public boolean isValid(String value) {
        return !TextUtils.isEmpty(value);
    }

    @Override
    public String getDescription() {
        return "Field must not be empty";
    }
}