package com.grevi.aywapet.db

import com.grevi.aywapet.db.entity.Users

interface DatabaseHelper {
    suspend fun insertUser(users: Users)
    suspend fun getUser() : MutableList<Users>
    suspend fun deleteUser(email : String)
}