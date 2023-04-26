package com.pet.shelter.friends;

import com.google.android.material.textfield.TextInputLayout;

public interface ErrorSetter {
    void setError(TextInputLayout textInputLayout, String errorMessage);
}
