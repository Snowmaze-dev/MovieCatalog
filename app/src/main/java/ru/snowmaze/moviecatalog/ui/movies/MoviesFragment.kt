package ru.snowmaze.moviecatalog.ui.movies

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnNextLayout
import androidx.core.view.isInvisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.snowmaze.moviecatalog.R
import ru.snowmaze.moviecatalog.databinding.FragmentMoviesBinding
import ru.snowmaze.moviecatalog.domain.Movie

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies), MovieItemCallback {

    private val viewModel: MoviesViewModel by activityViewModels()
    private val viewBinding: FragmentMoviesBinding by viewBinding()
    private val moviesAdapter = MoviesAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.movies.observe(viewLifecycleOwner) {
            moviesAdapter.items = it
        }
        with(viewBinding) {
            viewModel.isLoading.observe(viewLifecycleOwner) { state ->
                val isMoviesEmpty = viewModel.movies.value?.isEmpty() ?: true
                val currentQuery = viewModel.currentQuery
                val isLoading = state.isLoading
                if (!isLoading) {
                    swipeToRefreshMovies.isRefreshing = false
                    if (loadingProgress.isAnimatingProgress) {
                        loadingProgress.animateProgressToEnd(duration = 500) {
                            loadingProgress.isInvisible = true
                            loadingProgress.progress = 0
                        }
                    }
                }
                when {
                    isMoviesEmpty && isLoading -> stubView.showProgress()
                    state.error != null -> {
                        if (isMoviesEmpty) {
                            viewBinding.stubView.showRequestError {
                                viewModel.discoverMovies(viewModel.currentQuery)
                            }
                        } else {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.check_your_internet_connection_and_retry),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                    isMoviesEmpty && currentQuery != null -> stubView.showNothingFound(currentQuery)
                    !isMoviesEmpty && isLoading -> {
                        stubView.clearStub()
                        loadingProgress.animateProgressToEnd()
                    }
                    !isMoviesEmpty -> stubView.clearStub()
                }
            }
            (recyclerMovies.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
            recyclerMovies.layoutManager = LinearLayoutManager(requireContext())
            recyclerMovies.setHasFixedSize(true)
            recyclerMovies.adapter = moviesAdapter
            swipeToRefreshMovies.setOnRefreshListener {
                viewModel.discoverMovies(viewModel.currentQuery)
            }

            // чтобы не спамить запросами поиска
            var searchJob: Job? = null
            inputSearch.doOnNextLayout {
                inputSearch.doOnTextChanged { text, _, _, _ ->
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        delay(300)
                        viewModel.discoverMovies(text?.toString())
                    }
                }
            }
        }
    }

    override fun onMovieClick(movie: Movie) {
        Snackbar.make(requireView(), movie.title, Snackbar.LENGTH_SHORT).show()
    }

    override fun onFavoriteButtonClick(movie: Movie) {
        viewModel.onFavoriteClick(movie)
    }

    override fun onScrolledToEnd() {
        viewModel.loadNextPage()
    }
}