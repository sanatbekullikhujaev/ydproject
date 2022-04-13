package com.usc0der.ydprojectnew.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(RoomModel::class), version = 1)
abstract class AppDatabase :RoomDatabase() {

    abstract fun roomModel(): AppDao
    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        var LOCK = Any()
        fun getDataseClient(context: Context) : AppDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(LOCK) {

                INSTANCE = Room
                    .databaseBuilder(
                        context, AppDatabase::class.java,
                        "usc0der.db"
                    )
//                    .createFromAsset("regionTests.db")
                    .allowMainThreadQueries()
//                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
                    .build()

                return INSTANCE!!
            }
        }
    }

}