package com.example.submissionawal.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.submissionawal.data.local.entity.EventsEntity
import com.example.submissionawal.data.local.entity.FavEntity

@Database(entities = [EventsEntity::class,FavEntity::class], version = 1, exportSchema = false)
abstract class EventsDatabase : RoomDatabase() {
    abstract fun eventsDao(): EventsDao
    abstract fun favDao(): FavDao

    companion object {
        @Volatile
        private var instance: EventsDatabase? = null
        fun getInstance(context: Context): EventsDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    EventsDatabase::class.java, "Events.db"
                ).build()
            }
    }
}