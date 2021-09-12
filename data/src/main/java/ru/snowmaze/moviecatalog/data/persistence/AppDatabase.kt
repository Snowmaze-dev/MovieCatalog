package ru.snowmaze.moviecatalog.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MoviePersistenceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

}