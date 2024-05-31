package com.example.challenge4.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import kotlinx.coroutines.flow.catch
import android.widget.Toast


class DataStoreManager(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore("user_prefs")
        val NAME_KEY = stringPreferencesKey("name")
        val EMAIL_KEY = stringPreferencesKey("email")
        val PASSWORD_KEY = stringPreferencesKey("password")
        val MOBILE_PHONE_KEY = stringPreferencesKey("mobilePhone")
        val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
    }

    suspend fun storeUserData(userData: UserData) {
        try {
            context.dataStore.edit { preferences ->
                preferences[NAME_KEY] = userData.name
                preferences[EMAIL_KEY] = userData.email
                preferences[PASSWORD_KEY] = userData.password
                preferences[MOBILE_PHONE_KEY] = userData.mobilePhone
                preferences[IS_LOGIN_KEY] = userData.isLogin
            }
            Toast.makeText(context, "User data stored successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error storing user data: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    val userData: Flow<UserData?> = context.dataStore.data
        .catch { exception ->
            Toast.makeText(context, "Error reading user data: ${exception.message}", Toast.LENGTH_LONG).show()
        }
        .map { preferences ->
            val name = preferences[NAME_KEY] ?: ""
            val email = preferences[EMAIL_KEY] ?: ""
            val password = preferences[PASSWORD_KEY] ?: ""
            val mobilePhone = preferences[MOBILE_PHONE_KEY] ?: ""
            val isLogin = preferences[IS_LOGIN_KEY] ?: false
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && mobilePhone.isNotEmpty()) {
                UserData(name, email, password, mobilePhone, isLogin)
            } else {
                null
            }
        }

    suspend fun clearUserData() {
        try {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
            Toast.makeText(context, "User data cleared successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error clearing user data: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
