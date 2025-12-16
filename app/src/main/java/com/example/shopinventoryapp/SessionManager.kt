package com.example.shopinventoryapp

import android.content.Context

class SessionManager(context: Context) {
    private var prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)
    fun saveAdminLogin() {
        prefs.edit().apply() {
            putBoolean("is_log_in", true).putString("role", UserRole.ADMIN.name).apply()
        }
    }

    fun saveUserLogin(): Boolean {
        prefs.edit().apply() {
            putBoolean("is_log_in", true).putString("role", UserRole.USER.name)
                .apply()
        }
        return true
    }

    fun getUserRole(): UserRole {
        return when (prefs.getString("role", null)) {
            UserRole.ADMIN.name -> UserRole.ADMIN
            UserRole.USER.name -> UserRole.USER
            else -> UserRole.NONE
        }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_log_in", false)
    }


    fun logout() {
        prefs.edit().clear().apply()
    }
}