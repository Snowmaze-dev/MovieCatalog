package ru.snowmaze.moviecatalog.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import ru.snowmaze.moviecatalog.R
import ru.snowmaze.moviecatalog.ui.movies.MoviesFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager.commit {
                replace(R.id.mainFragmentContainer, MoviesFragment())
            }
        }
    }
}