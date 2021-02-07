package com.grevi.aywapet.db

import com.grevi.aywapet.db.entity.Users
import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {
    suspend fun insertUser(users: Users)
    suspend fun getUser() : Flow<MutableList<Users>>
    suspend fun deleteUser(email : String)
}