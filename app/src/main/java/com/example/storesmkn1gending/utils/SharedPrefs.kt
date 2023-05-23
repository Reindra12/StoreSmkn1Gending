package com.example.storesmkn1gending.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPrefs(private val context: Context) {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private const val TOKEN = "token"
    }

    var accessToken: String?
        get() = sharedPreferences.getString(TOKEN, null)
        set(value) {
            sharedPreferences.edit().putString(TOKEN, value).apply()
        }
}