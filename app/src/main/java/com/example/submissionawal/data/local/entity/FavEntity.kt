package com.example.submissionawal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
class FavEntity(
    @field:ColumnInfo(name = "id")
    @PrimaryKey
    var id: String,

    @field:ColumnInfo(name = "title")
    var title: String,
)