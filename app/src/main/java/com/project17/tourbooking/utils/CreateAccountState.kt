package com.project17.tourbooking.utils

import android.net.Uri

data class CreateAccountState(
    var fullName: String = "",
    var email: String = "",
    var username: String = "",
    var selectedGender: Boolean = true,
    var phoneNumber: String = "",
    var address: String = "",
    var dateOfBirth: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var passwordVisibility: Boolean = false,
    var confirmPasswordVisibility: Boolean = false,
    var selectedImageUri: Uri? = null
)