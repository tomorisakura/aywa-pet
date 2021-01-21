package com.grevi.aywapet.repository

import com.grevi.aywapet.db.DatabaseHelperImpl
import com.grevi.aywapet.db.entity.Users
import javax.inject.Inject

class LocalRepos @Inject constructor(private val databaseHelperImpl: DatabaseHelperImpl) {
    suspend fun insertUser(users: Users) {
        databaseHelperImpl.insertUser(users)
    }

    suspend fun getUser() : MutableList<Users> {
       return databaseHelperImpl.getUser()
    }

    suspend fun deleteUser(email : String) = databaseHelperImpl.deleteUser(email)
}