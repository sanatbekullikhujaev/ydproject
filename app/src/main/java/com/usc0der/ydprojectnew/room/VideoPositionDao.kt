package com.usc0der.ydprojectnew.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoPositionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(videoDbModel: VideoDbModel)

    @Query("DELETE FROM videoposition_table WHERE id = :id")
    fun deleteById(id: Int)

     @Query("SELECT * FROM videoposition_table WHERE id = :id")
    fun getObjPositionId(id: Int):VideoDbModel

}