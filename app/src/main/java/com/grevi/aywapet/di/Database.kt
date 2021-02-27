package com.grevi.aywapet.di

import android.content.Context
import androidx.room.Room
import com.grevi.aywapet.db.AppDatabase
import com.grevi.aywapet.db.DatabaseDAO
import com.grevi.aywapet.db.DatabaseHelper
import com.grevi.aywapet.db.DatabaseHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Database {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "aywapetDB")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabaseDAO(database: AppDatabase) : DatabaseDAO {
        return database.databaseDAO()
    }

    @Provides
    @Singleton
    fun provideDatabaseHelper(databaseHelperImpl: DatabaseHelperImpl) : DatabaseHelper {
        return databaseHelperImpl
    }
}