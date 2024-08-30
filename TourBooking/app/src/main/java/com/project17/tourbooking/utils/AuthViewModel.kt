package com.project17.tourbooking.utils

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    fun isAdmin(): Boolean {
        val user = auth.currentUser
        return user?.let {
            val customClaims = user.getIdToken(true).result?.claims
            customClaims?.get("admin") == true
        } ?: false
    }

    fun isUser(): Boolean {
        val user = auth.currentUser
        return user?.let {
            val customClaims = user.getIdToken(true).result?.claims
            customClaims?.get("user") == true
        } ?: false
    }
}
