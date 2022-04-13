package com.usc0der.ydprojectnew.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videoposition_table")
class VideoDbModel(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    @ColumnInfo(name = "video_position")
    var videoPosition:Long,
)