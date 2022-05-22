package com.pthw.mymovieapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pthw.mymovieapp.data.local.dao.HomeMovieDao
import com.pthw.mymovieapp.data.local.typeconverter.MovieTypeConverter
import com.pthw.mymovieapp.vos.HomeMovieVO

@Database(entities = [HomeMovieVO::class], version = 1)
@TypeConverters(
    MovieTypeConverter::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getHomeMovieDao(): HomeMovieDao

    companion object {

        const val DB_NAME = "MYMOVIEAPP.DB"

        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
//                INSTANCE = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
//                    .fallbackToDestructiveMigration()
//                    .allowMainThreadQueries()
//                    .build()
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE as AppDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}