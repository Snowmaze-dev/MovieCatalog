package ru.snowmaze.moviecatalog.ui.movies

import androidx.recyclerview.widget.DiffUtil
import ru.snowmaze.moviecatalog.domain.Movie

class MovieItemDiffCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return newItem.isFavorite == oldItem.isFavorite
    }
}