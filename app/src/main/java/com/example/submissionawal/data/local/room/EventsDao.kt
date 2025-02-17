package com.example.submissionawal.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.submissionawal.data.local.entity.EventsEntity

@Dao
interface EventsDao {

    @Query("SELECT * FROM events ORDER BY beginTime DESC")
    fun getEvents(): LiveData<List<EventsEntity>>

    @Query("SELECT * FROM events where favorited = 1")
    fun getFavoritedEvents(): LiveData<List<EventsEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    fun getFavId(id: String): LiveData<List<EventsEntity>>


    @Query("SELECT EXISTS(SELECT * FROM events WHERE title = :title AND favorited = 1)")
    fun isFavorited(title: String): String

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvents(events: List<EventsEntity>)

    @Update
    suspend fun updateEvents(events: EventsEntity)

    @Query("DELETE FROM events WHERE favorited = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM events WHERE title = :title AND favorited = 1)")
    suspend fun isEventsFavorited(title: String): Boolean

}