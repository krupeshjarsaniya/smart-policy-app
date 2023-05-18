package com.example.policyagent.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.policyagent.data.database.entities.User


@Database(entities = [User::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) { instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "restaurant.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }
}