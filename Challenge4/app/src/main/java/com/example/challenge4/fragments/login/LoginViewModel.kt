package com.example.challenge4.fragments.login

import androidx.lifecycle.ViewModel
import android.content.Context
import android.widget.Toast
import com.example.challenge4.data.UserData

class LoginViewModel : ViewModel() {
    private var email: String = "";
    private var password: String = "";

    fun getEmailPasswordFromPreferences(context: Context): Pair<String, String> {
        val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "") ?: ""
        val password = sharedPreferences.getString("password", "") ?: ""
        return Pair(email, password)
    }

    private fun getUserDataFromPreferences(context: Context): UserData {
        val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        return UserData(
            sharedPreferences.getString("name", "") ?: "",
            sharedPreferences.getString("email", "") ?: "",
            sharedPreferences.getString("password", "") ?: "",
            sharedPreferences.getString("mobilePhone", "") ?: "",
            sharedPreferences.getBoolean("isLogin", false)
        )
    }

    fun checkLogin(email: String, password: String, context: Context): Boolean {
        val userData = getUserDataFromPreferences(context)
        tryShowPreference(context)
        return if (userData.isLogin) {
            true // Already logged in
        } else {
            // Check if provided email and password match stored data
            val sharedPreferences = context.getSharedPreferences("UserData", 0)
            val storedEmail = sharedPreferences.getString("email", "")
            val storedPassword = sharedPreferences.getString("password", "")
            if (email == storedEmail && password == storedPassword) {
                // Update isLogin in SharedPreferences and navigate to next screen
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLogin", true)
                editor.apply()
                true // Login successful
            } else {
                false // Email or password incorrect
            }
        }
    }

    private fun tryShowPreference(context: Context){
        val userData = getUserDataFromPreferences(context)
        // show using Toast
        Toast.makeText(context, "Name: ${userData.name}", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "Email: ${userData.email}", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "Password: ${userData.password}", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "Mobile Phone: ${userData.mobilePhone}", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "Is Login: ${userData.isLogin}", Toast.LENGTH_SHORT).show()
    }
}