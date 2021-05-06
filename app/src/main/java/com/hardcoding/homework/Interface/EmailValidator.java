package com.hardcoding.homework.Interface;

import android.util.Patterns;

public class EmailValidator implements Validator<String> {

    @Override
    public boolean isValid(String value) {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

    @Override
    public String getDescription() {
        return "Email should be in \'a@a.com\' format";
    }
}
