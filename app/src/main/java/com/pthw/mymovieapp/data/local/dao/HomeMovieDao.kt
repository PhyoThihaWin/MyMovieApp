package com.pthw.mymovieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pthw.mymovieapp.vos.HomeMovieVO

@Dao
interface HomeMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (vo : HomeMovieVO)

    @Query("delete from home_movie_table")
    fun deleteAll ()

    @Query("select * from home_movie_table")
    fun getHomeMovie () : HomeMovieVO?

}