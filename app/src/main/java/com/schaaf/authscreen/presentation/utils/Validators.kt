package com.schaaf.authscreen.presentation.utils

import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout
import com.schaaf.authscreen.R

fun TextInputLayout.validateEmail(): Boolean {

    editText?.text?.let { editable ->

        return if (editable.toString().isEmpty() || editable.toString().isBlank() ) {
            error = context.getString(R.string.error_registration_email_empty)
            false

        } else if (!Patterns.EMAIL_ADDRESS.matcher(editable.trim { it <= ' ' }).matches()) {
            error = context.getString(R.string.error_registration_email_incorrect)
            false

        } else {
            true
        }
    }
    error = context.getString(R.string.error_registration_email_empty)
    return false
}


fun TextInputLayout.validatePassword(): Boolean {

    editText?.text?.toString()?.let { string ->

        return if (string.isEmpty() || string.isBlank()) {
            error = context.getString(R.string.error_registration_password_empty)
            false

        } else if (string.length <= 6) {
            error = context.getString(R.string.error_registration_password_length)
            false

        } else if (string.find { it.isDigit() } == null) {
            error = context.getString(R.string.error_registration_password_number)
            false

        } else if (string.find { it.isUpperCase() } == null) {
            error = context.getString(R.string.error_registration_password_uppercase_letter)
            false

        } else if (string.find { it.isLowerCase() } == null) {
            error = context.getString(R.string.error_registration_password_lowercase_letter)
            false

        } else {
            true
        }
    }
    error = context.getString(R.string.error_registration_email_empty)
    return false
}