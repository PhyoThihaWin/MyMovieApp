package com.pthw.mymovieapp.di

import android.content.Context
import androidx.room.Room
import com.pthw.mymovieapp.BuildConfig
import com.pthw.mymovieapp.data.local.db.AppDatabase
import com.pthw.mymovieapp.data.network.service.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {


    @Module
    @InstallIn(SingletonComponent::class)
    object Provider {


        @Provides
        @Named("MovieClient")
        @Singleton
        fun provideEysClientRetrofit(@Named("ClientOkhttp") client: OkHttpClient): Retrofit {
            return createRetrofitClient(BuildConfig.BASE_URL, client)
        }

        @Provides
        @Named("ClientOkhttp")
        @Singleton
        fun provideClientOkhttpClient(
            @ApplicationContext context: Context
        ): OkHttpClient {
            return createOkHttpClient(context)
        }

        @Provides
        @Singleton
        fun provideMovieService(@Named("MovieClient") retrofit: Retrofit): MovieService {
            return retrofit.create(MovieService::class.java)
        }

        @Singleton
        @Provides
        fun provideYourDatabase(
            @ApplicationContext app: Context
        ) = Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()

        @Singleton
        @Provides
        fun provideYourDao(db: AppDatabase) = db.getHomeMovieDao()

    }


}