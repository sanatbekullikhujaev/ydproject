package com.usc0der.ydprojectnew.room

import androidx.room.*

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(region: RoomModel)

    @Update
    fun update(region: RoomModel)

    @Delete
    fun delete(region: RoomModel)
    @Query("select * from usc0der_table")
    fun getAll():List<RoomModel>

    @Query("DELETE FROM usc0der_table WHERE id = :id")
    fun deleteById(id: Int)
    @Query("SELECT * FROM usc0der_table WHERE id = :id")
    fun getObjRegionById(id: Int):RoomModel

}