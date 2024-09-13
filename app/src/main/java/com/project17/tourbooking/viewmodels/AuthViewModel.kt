package com.project17.tourbooking.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project17.tourbooking.models.Account

class AuthViewModel : ViewModel() {
    public val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _isAdmin = MutableLiveData<Boolean>()
    val isAdmin: LiveData<Boolean> = _isAdmin

    private val _isUser = MutableLiveData<Boolean>()
    val isUser: LiveData<Boolean> = _isUser

    init {
        checkAuthStatus()
    }

    val currentUserEmail: String? get() = auth.currentUser?.email

    fun checkAuthStatus() {
        if (auth.currentUser != null) {
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun fetchUserRole(email: String) {
        firestore.collection("accounts")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val account = document.toObject(Account::class.java)
                    if (account != null) {
                        when (account.role) {
                            "admin" -> {
                                _isAdmin.value = true
                                _isUser.value = false
                                _authState.value = AuthState.Authenticated
                            }
                            "user" -> {
                                _isUser.value = true
                                _isAdmin.value = false
                                _authState.value = AuthState.Authenticated
                            }
                            else -> {
                                _isAdmin.value = false
                                _isUser.value = false
                                _authState.value = AuthState.Error("Unknown role")
                            }
                        }
                    }
                } else {
                    _authState.value = AuthState.Error("Account not found")
                }
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Failed to fetch role")
            }
    }



    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fetchUserRole(email)
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.SignUpSuccess
                    _authState.value = AuthState.Unauthenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
        _isAdmin.value = false
        _isUser.value = false
    }
}

sealed class AuthState{
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message: String): AuthState()
    object SignUpSuccess : AuthState()
}
