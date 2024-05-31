package com.example.challenge4.data.userlogin

import androidx.lifecycle.LiveData
import com.example.challenge4.data.userlogin.User
import com.example.challenge4.data.userlogin.UserDao

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

}