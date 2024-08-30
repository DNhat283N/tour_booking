package com.project17.tourbooking.utils

import org.mindrot.jbcrypt.BCrypt

object PasswordUtils {

    // Hash mật khẩu
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    // Kiểm tra mật khẩu
    fun checkPassword(password: String, hashed: String): Boolean {
        return BCrypt.checkpw(password, hashed)
    }
}
