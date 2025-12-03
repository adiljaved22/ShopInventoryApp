package com.example.shopinventoryapp

import android.content.Context

class SessionManager(context: Context) {
    private var prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)
    fun saveLogin(): Boolean {
        prefs.edit().apply() {
            putBoolean("is_log_in", true)
            apply()
        }
        return true
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_log_in", false)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}