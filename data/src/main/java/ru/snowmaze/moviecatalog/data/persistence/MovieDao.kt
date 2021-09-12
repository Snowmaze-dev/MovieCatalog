package ru.snowmaze.moviecatalog.data.persistence

import androidx.room.*

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: MoviePersistenceEntity)

    @Delete
    suspend fun remove(product: MoviePersistenceEntity)

    @Query("SELECT * FROM MoviePersistenceEntity WHERE isFavorite = 1")
    suspend fun getAllFavorites(): List<MoviePersistenceEntity>

}