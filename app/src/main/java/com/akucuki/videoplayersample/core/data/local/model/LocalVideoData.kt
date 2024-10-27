package com.akucuki.videoplayersample.core.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos_table")
data class LocalVideoData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int = 0,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("subtitle")
    val subtitle: String,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("shortThumbnailUrl")
    val shortThumbnailUrl: String,
    @ColumnInfo("sourceUrl")
    val sourceUrl: String?
)
