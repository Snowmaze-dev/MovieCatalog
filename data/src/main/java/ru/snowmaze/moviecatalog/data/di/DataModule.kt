package ru.snowmaze.moviecatalog.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import ru.snowmaze.moviecatalog.data.BuildConfig
import ru.snowmaze.moviecatalog.data.network.MoviesAPIService
import ru.snowmaze.moviecatalog.data.MoviesRepositoryImpl
import ru.snowmaze.moviecatalog.data.network.sources.MoviesNetworkSource
import ru.snowmaze.moviecatalog.data.network.sources.MoviesNetworkSourceImpl
import ru.snowmaze.moviecatalog.data.persistence.AppDatabase
import ru.snowmaze.moviecatalog.data.persistence.MovieDao
import ru.snowmaze.moviecatalog.data.persistence.MoviesPersistenceSource
import ru.snowmaze.moviecatalog.data.persistence.MoviesPersistenceSourceImpl
import ru.snowmaze.moviecatalog.domain.MoviesRepository

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideMoviesRetrofitService(): MoviesAPIService {
        return Retrofit.Builder()
            .client(OkHttpClient.Builder().apply {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.BASIC
                addInterceptor(interceptor)
            }.build())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "MovieCatalog").build()
    }

    @Provides
    fun provideMoviesDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.getMovieDao()
    }

    @Provides
    fun provideMoviesPersistenceSource(
        movieDao: MovieDao
    ): MoviesPersistenceSource = MoviesPersistenceSourceImpl(movieDao)

    @Provides
    fun provideMoviesNetworkSource(
        apiService: MoviesAPIService
    ): MoviesNetworkSource = MoviesNetworkSourceImpl(apiService)

    @Provides
    fun provideMoviesRepository(
        networkSource: MoviesNetworkSource,
        moviesPersistenceSource: MoviesPersistenceSource
    ): MoviesRepository = MoviesRepositoryImpl(networkSource, moviesPersistenceSource)
}