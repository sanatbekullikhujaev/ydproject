package com.usc0der.ydprojectnew.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [VideoDbModel::class], version = 1)
abstract class VideoAppDatabase :RoomDatabase() {

    abstract fun videoPosition():VideoPositionDao
    companion object {
        @Volatile
        private var INSTANCE: VideoAppDatabase? = null

        var LOCK = Any()
        fun getDataseClient(context: Context) : VideoAppDatabase {

            if (INSTANCE != null) return INSTANCE!!
            synchronized(LOCK) {

                INSTANCE = Room
                    .databaseBuilder(
                        context, VideoAppDatabase::class.java,
                        "videoposition.db"
                    )
//                    .createFromAsset("regionTests.db")
                    .allowMainThreadQueries()
//                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!
            }
        }
    }
}