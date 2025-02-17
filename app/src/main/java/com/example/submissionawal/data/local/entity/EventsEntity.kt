package com.example.submissionawal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
class EventsEntity(

    @field:ColumnInfo(name = "id")
    @PrimaryKey
    var id: String,

    @field:ColumnInfo(name = "title")
    var title: String,

    @field:ColumnInfo(name = "category")
    var category: String,

    @field:ColumnInfo(name = "summary")
    var summary: String,

    @field:ColumnInfo(name = "imageLogo")
    var imageLogo: String? = null,

    @field:ColumnInfo(name = "beginTime")
    var beginTime: String,

    @field:ColumnInfo(name = "quota")
    var quota: String,

    @field:ColumnInfo(name = "ownerName")
    var ownerName: String,

    @field:ColumnInfo(name = "registrants")
    var registrants: String,

    @field:ColumnInfo(name = "mediaCover")
    var mediaCover: String? = null,

    @field:ColumnInfo(name = "description")
    var description: String,

    @field:ColumnInfo(name = "link")
    var link: String,

    @field:ColumnInfo(name = "favorited")
    var isFavorited: Boolean
)