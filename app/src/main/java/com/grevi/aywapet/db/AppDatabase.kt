package com.grevi.aywapet.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.grevi.aywapet.db.entity.Users

@Database(entities = [Users::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDAO() : DatabaseDAO
}