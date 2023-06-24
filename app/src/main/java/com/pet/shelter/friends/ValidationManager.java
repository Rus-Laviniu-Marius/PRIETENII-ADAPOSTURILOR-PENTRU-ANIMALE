package com.pet.shelter.friends;


import android.content.Context;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ValidationManager {

    private static ValidationManager instance = null;
    private TextInputLayout textInputLayout;
    private EditText editText;
    private ErrorSetter errorSetter;
    private static final int phoneNumberDigits = 10;
    private static final int minimumPasswordLength = 14;
    private static final String emailPattern = "[a-zA-Z0-9._-][a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String ERROR_MESSAGE_CHECK_EMPTY = "Field can not be empty.";
    private static final String ERROR_MESSAGE_CHECK_PHONE_NUMBER = "Please enter 10 digit phone number.";
    private static final String ERROR_MESSAGE_CHECK_PHONE_NUMBER_REMOVE_STARTING_PLUS = "Please remove \"+\" starting character";
    private static final String ERROR_MESSAGE_CHECK_EMAIL = "Invalid email address.";
    private static final String ERROR_MESSAGE_CHECK_MATCH_PASSWORD = "Password does not match.";
    private static final String ERROR_MESSAGE_CHECK_SHORT_PASSWORD = "Be at least 14 characters long.";
    private static final String ERROR_MESSAGE_CHECK_PASSWORD_WITH_NO_DIGITS = "Include at least one digit";
    private static final String ERROR_MESSAGE_CHECK_PASSWORD_WITH_NO_LOWER_CHARACTERS = "Include at least one lower character";
    private static final String ERROR_MESSAGE_CHECK_PASSWORD_WITH_NO_UPPER_CHARACTERS = "Include at least one upper character";
    private static final String ERROR_MESSAGE_CHECK_PASSWORD_WITH_NO_SPECIAL_CHARACTERS = "Include at least one special character";


    private boolean isEmpty = false;
    private boolean isEmptyValid = false;
    private boolean isEmailValid = false;
    private boolean isPhoneNumberValid = false;
    private boolean isPasswordValid = false;
    private boolean isPasswordMatchValid = false;
    private boolean isIbanValid = false;

    /* implementation of singleton object creation pattern so this class has a single object through
    app lifecycle */
    private ValidationManager() {}

    public static ValidationManager getInstance() {
        if (instance == null) {
            instance = new ValidationManager();
        }
        return instance;
    }

    public ValidationManager doValidation(Context context, TextInputLayout textInputLayout) {
        errorSetter = (ErrorSetter) context;
        this.textInputLayout = textInputLayout;
        this.editText = textInputLayout.getEditText();
        return instance;
    }

    public ValidationManager checkEmpty() {
        if (editText.getText().toString().isEmpty()) {
            errorSetter.setError(textInputLayout, ERROR_MESSAGE_CHECK_EMPTY);
            isEmpty = true;
            isEmptyValid = false;
        } else {
            isEmpty = false;
            isEmptyValid = true;
        }

        return instance;
    }

    public void checkPhoneNumber() {
        if (!isEmpty && editText.getText().toString().length() != phoneNumberDigits) {
            errorSetter.setError(textInputLayout, ERROR_MESSAGE_CHECK_PHONE_NUMBER);
            isPasswordValid = false;
        } else if (!isEmpty && editText.getText().toString().startsWith("+")){
            errorSetter.setError(textInputLayout, ERROR_MESSAGE_CHECK_PHONE_NUMBER_REMOVE_STARTING_PLUS);
            isPhoneNumberValid = false;
        } else {
            isPhoneNumberValid = true;
        }
    }

    public void checkEmail() {
        if (!isEmpty && !editText.getText().toString().matches(emailPattern)) {
            errorSetter.setError(textInputLayout, ERROR_MESSAGE_CHECK_EMAIL);
            isEmailValid = false;
        } else {
            isEmailValid = true;
        }

    }

    public void checkStrongPassword() {
        String password = editText.getText().toString();
        boolean hasDigit = false;
        boolean hasSpecial = false;
        boolean hasLower = false;
        boolean hasUpper = false;
        char c;
        for (int i = 0; i < password.length(); i++) {
            c = password.charAt(i);
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (Character.isLowerCase(c)) {
                hasLower = true;
            }
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            }
            if (!Character.isUpperCase(c) && !Character.isLowerCase(c) && !Character.isDigit(c) && !Character.isSpaceChar(c)) {
                hasSpecial = true;
            }
        }

        if (!isEmpty && password.length() < minimumPasswordLength) {
            errorSetter.setError(textInputLayout, ERROR_MESSAGE_CHECK_SHORT_PASSWORD);
            isPasswordValid = false;
        } else if (!isEmpty  && !hasDigit) {
            errorSetter.setError(textInputLayout, ERROR_MESSAGE_CHECK_PASSWORD_WITH_NO_DIGITS);
            isPasswordValid = false;
        } else if (!isEmpty  && !hasLower) {
            errorSetter.setError(textInputLayout, ERROR_MESSAGE_CHECK_PASSWORD_WITH_NO_LOWER_CHARACTERS);
            isPasswordValid = false;
        } else if (!isEmpty && !hasUpper) {
            errorSetter.setError(textInputLayout, ERROR_MESSAGE_CHECK_PASSWORD_WITH_NO_UPPER_CHARACTERS);
            isPasswordValid = false;
        } else if (!isEmpty && !hasSpecial) {
            errorSetter.setError(textInputLayout, ERROR_MESSAGE_CHECK_PASSWORD_WITH_NO_SPECIAL_CHARACTERS);
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
        }
    }

    public void matchPassword(TextInputLayout firstPasswordLayout, TextInputLayout secondPasswordLayout) {
        if (!isEmpty && !Objects.requireNonNull(firstPasswordLayout.getEditText()).getText().toString()
                .equals(Objects.requireNonNull(secondPasswordLayout.getEditText()).getText().toString())) {
            errorSetter.setError(textInputLayout, ERROR_MESSAGE_CHECK_MATCH_PASSWORD);
            isPasswordMatchValid = false;
        } else {
            isPasswordMatchValid = true;
        }
    }

    private void checkIbanValid(String iban) {

        int IBAN_MIN_SIZE = 15;
        int IBAN_MAX_SIZE = 34;
        long IBAN_MAX = 999999999;
        long IBAN_MODULUS = 97;

        String trimmed = iban.trim();

        if (trimmed.length() < IBAN_MIN_SIZE || trimmed.length() > IBAN_MAX_SIZE) {
            isIbanValid = false;
        }

        String reformat = trimmed.substring(4) + trimmed.substring(0, 4);
        long total = 0;

        for (int i = 0; i < reformat.length(); i++) {

            int charValue = Character.getNumericValue(reformat.charAt(i));

            if (charValue < 0 || charValue > 35) {
                isIbanValid = false;
            }

            total = (charValue > 9 ? total * 100 : total * 10) + charValue;

            if (total > IBAN_MAX) {
                total = (total % IBAN_MODULUS);
            }
        }

        isIbanValid = (total % IBAN_MODULUS) == 1;
    }

    public boolean isNothingEmpty() {
        return isEmptyValid;
    }
    public boolean isEmailAndPasswordValid() {
        return isEmptyValid && isEmailValid && isPasswordValid;
    }

    public boolean isAllValid() {

        return isEmptyValid && isEmailValid && isPasswordMatchValid && isPasswordValid;
    }

    public boolean isPhoneNumberValidAndNothingEmpty() {
        return isEmptyValid && isPhoneNumberValid;
    }

    public boolean isAllValidForShelterProfile() {
        return isEmptyValid && isIbanValid;
    }

    public boolean arePhoneNumberAndEmailValidAndNothingEmpty() {
        return isEmptyValid && isPhoneNumberValid && isEmailValid;
    }
}
