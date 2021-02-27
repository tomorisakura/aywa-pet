package com.grevi.aywapet.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class Users(
        @PrimaryKey @ColumnInfo(name ="id") var id : String,
        @ColumnInfo(name = "username") var username : String,
        @ColumnInfo(name = "email") var email : String,
        @ColumnInfo(name = "name") var name : String,
        @ColumnInfo(name="address") var address : String,
        @ColumnInfo(name = "token") var token : String,
        @ColumnInfo(name = "uid") var uid : String
)
