package com.example.challenge4.fragments.register

import androidx.lifecycle.ViewModel
import android.widget.Toast
import android.content.Context
import com.example.challenge4.data.UserData


class RegisterViewModel : ViewModel() {
    suspend fun registerUser(context: Context,name: String, email: String, password: String, mobilePhone: String): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || mobilePhone.isEmpty()) {
            alertUser(context,"Please fill in all fields")
            return false
        }
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        if (!email.matches(emailRegex)) {
            alertUser(context, "Please enter a valid email address")
            return false
        }
        val userData = UserData(name, email, password, mobilePhone)
        storeUserDataCookies(context, userData)
        return true
    }

    private  fun storeUserDataCookies(context: Context, userData: UserData) {
        // Store user data in SharedPreferences or any other storage mechanism
        // add the code
        val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", userData.name)
        editor.putString("email", userData.email)
        editor.putString("password", userData.password)
        editor.putString("mobilePhone", userData.mobilePhone)
        editor.putBoolean("isLogin", userData.isLogin)
        editor.apply()
        alertUser(context, "Successfully Registered")

    }

     fun alertUser(context: Context,message: String) {
         val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
         toast.show()
    }
}
