package com.usc0der.ydprojectnew.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "usc0der_table")
class RoomModel (
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    @ColumnInfo(name = "path")
    var path:String,
    @ColumnInfo(name = "extension")
    var extension:String,
)
