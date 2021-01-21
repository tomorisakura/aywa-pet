package com.grevi.aywapet.db

import com.grevi.aywapet.db.entity.Users
import javax.inject.Inject

class DatabaseHelperImpl @Inject constructor(private val databaseDAO: DatabaseDAO) : DatabaseHelper {
    override suspend fun insertUser(users: Users) {
        databaseDAO.insertUsers(users)
    }

    override suspend fun getUser(): MutableList<Users> {
        return databaseDAO.getUsers()
    }

    override suspend fun deleteUser(email: String) {
        databaseDAO.deleteUser(email)
    }

}