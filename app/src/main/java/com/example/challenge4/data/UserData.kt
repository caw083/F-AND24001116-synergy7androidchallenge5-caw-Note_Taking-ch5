package com.example.challenge4.data

data class UserData(
    val name: String,
    val email: String,
    val password: String,
    val mobilePhone: String,
    var isLogin: Boolean = false
)