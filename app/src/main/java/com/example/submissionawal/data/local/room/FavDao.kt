package com.example.submissionawal.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.submissionawal.data.local.entity.FavEntity

@Dao
interface FavDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFav(fav: FavEntity)

    @Query("DELETE FROM favorite WHERE title = :title")
    suspend fun deleteFavoriteEvent(title: String)
}