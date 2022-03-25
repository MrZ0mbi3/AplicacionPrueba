package com.example.aplicacionprueba.vista

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.example.aplicacionprueba.R
import com.example.aplicacionprueba.model.Movie
import com.example.aplicacionprueba.viewmodel.MovieViewModel

class MainActivity : AppCompatActivity() {
    private val movieListViewModel: MovieViewModel by viewModels(factoryProducer = {
        MovieViewModel.Factory(appContext = this.applicationContext)
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_square -> {
            // User chose the "Settings" item, show the app settings UI...
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    fun updateFavorite(movie: Movie, isChecked:Boolean){
        movieListViewModel.updateFavoriteMovieUser(movie,isChecked)
    }
}