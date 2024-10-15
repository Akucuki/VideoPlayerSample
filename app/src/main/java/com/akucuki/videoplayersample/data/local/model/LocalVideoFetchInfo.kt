package com.akucuki.videoplayersample.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.Instant

@Entity(tableName = "videos_fetch_info_table")
data class LocalVideoFetchInfo(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Int = 1,
    @ColumnInfo("lastFetchDate")
    @TypeConverters(InstantConverter::class)
    val lastFetchDate: Instant? = null
)
