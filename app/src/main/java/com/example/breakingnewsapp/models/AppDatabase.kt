package com.example.breakingnewsapp.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.breakingnewsapp.api.ArticleDao

@Database(entities = [Article::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    // Declare associated DAOs
    abstract fun articleDao(): ArticleDao

    companion object {
        // Singleton pattern prevents multiple instances of the database
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Single thread-safe instance
            return INSTANCE ?: synchronized(this) {
                // Create database only if needed
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "article_database"
                ).build()

                // Save instance for future calls
                INSTANCE = instance
                instance
            }
        }
    }
}