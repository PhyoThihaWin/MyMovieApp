package com.pthw.mymovieapp.vos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_movie_table")
data class HomeMovieVO(
    @PrimaryKey(autoGenerate = true)
    var key: Int = 0,

    @ColumnInfo(name = "trend_list")
    val trendingList: List<MovieVO>,

    @ColumnInfo(name = "top_rated_list")
    val topRatedList: List<MovieVO>,

    @ColumnInfo(name = "upcoming_list")
    val upcomingList: List<MovieVO>,
)
