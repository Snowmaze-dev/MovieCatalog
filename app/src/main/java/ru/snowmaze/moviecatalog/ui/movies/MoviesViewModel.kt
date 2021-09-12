package ru.snowmaze.moviecatalog.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.snowmaze.moviecatalog.Constants.LANGUAGE
import ru.snowmaze.moviecatalog.domain.Movie
import ru.snowmaze.moviecatalog.domain.MoviesRepository
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) :
    ViewModel() {

    private val _movies = MutableLiveData(listOf<Movie>())
    private var page = 1
    private var _currentQuery: String? = null
    private var _isLoading = MutableLiveData(LoadingState())

    val isLoading: LiveData<LoadingState> get() = _isLoading
    val movies: LiveData<List<Movie>> = _movies
    val currentQuery get() = _currentQuery

    init {
        discoverMovies()
    }

    fun discoverMovies(query: String? = null) {
        var finalQuery = query
        if (query?.isBlank() == true) {
            finalQuery = null
        }
        page = 1
        _currentQuery = finalQuery
        viewModelScope.launch {
            _isLoading.value = LoadingState(isLoading = true)
            try {
                val m = moviesRepository.discoverMovies(
                    searchQuery = currentQuery,
                    page = page,
                    language = LANGUAGE
                )
                _movies.value = m
                _isLoading.value = LoadingState(isLoading = false)
            } catch (exception: Exception) {
                _isLoading.value = LoadingState(isLoading = false, exception)
            }
        }
    }

    fun loadNextPage() {
        page++
        viewModelScope.launch {
            kotlin.runCatching {
                _movies.value = withContext(Dispatchers.IO) {
                    (movies.value ?: emptyList()) + moviesRepository.discoverMovies(
                        searchQuery = currentQuery,
                        page = page,
                        language = LANGUAGE
                    )
                }
            }
        }
    }

    fun onFavoriteClick(clickedMovie: Movie) {

        // TODO можно приделать обновление через payload в адаптере
        viewModelScope.launch(Dispatchers.IO) {
            val movie = clickedMovie.copy(isFavorite = !clickedMovie.isFavorite)
            val newList = (_movies.value ?: emptyList()).map {
                if (it.id == movie.id) movie
                else it
            }
            withContext(Dispatchers.Main) {
                _movies.value = newList.toList()
            }
            if (movie.isFavorite) {
                moviesRepository.addFavorite(movie)
            } else {
                moviesRepository.removeFavorite(movie)
            }
        }
    }
}