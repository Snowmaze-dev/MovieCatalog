package ru.snowmaze.moviecatalog.ui.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import ru.snowmaze.moviecatalog.R
import ru.snowmaze.moviecatalog.databinding.ItemMovieBinding
import ru.snowmaze.moviecatalog.domain.Movie

class MoviesAdapter(private val callback: MovieItemCallback) :
    ListAdapter<Movie, MoviesAdapter.MovieVH>(MovieItemDiffCallback()) {

    var items: List<Movie>
        get() = currentList
        set(value) {
            submitList(value)
        }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val holder = MovieVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
        holder.viewBinding.btnMovieFavorite.setOnClickListener {
            callback.onFavoriteButtonClick(items[holder.absoluteAdapterPosition])
        }
        holder.viewBinding.root.setOnClickListener {
            callback.onMovieClick(items[holder.absoluteAdapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        holder.bind(getItem(position))
        if (PAGINATION_VISIBILITY_THRESHOLD >= itemCount - position) {
            callback.onScrolledToEnd()
        }
    }

    override fun getItemId(position: Int) = items[position].id.toLong()

    class MovieVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val viewBinding: ItemMovieBinding by viewBinding()

        init {
            viewBinding.root.clipToOutline = true
            viewBinding.root.outlineProvider = ViewOutlineProvider.BACKGROUND
        }

        fun bind(movie: Movie) {
            with(viewBinding) {
                movieTitle.text = movie.title
                movieOverview.text = movie.overview
                if (movie.releaseDate.isBlank()) {
                    movieReleaseDate.isInvisible = true
                } else {
                    movieReleaseDate.text = movie.releaseDate
                    movieReleaseDate.isVisible = true
                }
                Glide.with(moviePoster).load(movie.posterPath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transition(withCrossFade())
                    .into(moviePoster)
                btnMovieFavorite.setImageResource(
                    if (movie.isFavorite) R.drawable.ic_heart
                    else R.drawable.ic_heart_outlined
                )
            }
        }
    }

    companion object {

        const val PAGINATION_VISIBILITY_THRESHOLD = 4

    }
}

interface MovieItemCallback {

    fun onMovieClick(movie: Movie)

    fun onFavoriteButtonClick(movie: Movie)

    fun onScrolledToEnd()

}