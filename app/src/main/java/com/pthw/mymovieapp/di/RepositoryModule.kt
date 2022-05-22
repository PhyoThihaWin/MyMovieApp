package com.pthw.mymovieapp.di

import com.pthw.mymovieapp.data.repository.MovieRepository
import com.pthw.mymovieapp.data.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Singleton
    @Binds
    fun bindDeliveryRepository(repo: MovieRepositoryImpl): MovieRepository

}