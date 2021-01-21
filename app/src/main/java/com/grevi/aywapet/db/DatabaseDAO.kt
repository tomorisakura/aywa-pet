package com.grevi.aywapet.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grevi.aywapet.db.entity.Users

@Dao
interface DatabaseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(vararg users: Users)

    @Query("SELECT * FROM users_table")
    suspend fun getUsers() : MutableList<Users>

    @Query("DELETE FROM users_table WHERE `email` = :email")
    suspend fun deleteUser(email : String)
}