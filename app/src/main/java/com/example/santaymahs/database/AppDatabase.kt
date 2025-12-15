package com.example.santaymahs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// PERHATIKAN: Pastikan ada MoodEntry::class di sini!
@Database(entities = [User::class, HasilTes::class, MoodEntry::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "santay_mahs_database"
                )
                    .fallbackToDestructiveMigration() // PENTING AGAR TIDAK CRASH
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}