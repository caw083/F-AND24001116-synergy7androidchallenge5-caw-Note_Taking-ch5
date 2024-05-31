package com.example.challenge4.data.Notes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): NoteDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        ctx.applicationContext,                         NoteDatabase::class.java,
                        "app_database"
                    ).build()
                    INSTANCE = instance
                    instance
                }
            }
        }


    }



