package com.example.aplicacionprueba.controller

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionprueba.MdbApplication
import com.example.aplicacionprueba.R
import com.example.aplicacionprueba.model.Movie
import com.example.aplicacionprueba.vista.ActualMoviesFragment
import com.example.aplicacionprueba.vista.MainActivity

class MovieAdapter(val movies: MutableList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieHolder>() {


    class MovieHolder(val view: View) : RecyclerView.ViewHolder(view) {


        val textViewNameMovie: TextView
        val contenedorMovie: View
        val switch: Switch


        init {
            // Define click listener for the ViewHolder's View.
            textViewNameMovie = view.findViewById(R.id.textMovieName)
            switch = view.findViewById(R.id.switchFavoriteMovie)
            contenedorMovie = view

        }

    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.textViewNameMovie.text = movies[position].title

        holder.switch.setOnCheckedChangeListener(null)
        holder.switch.isChecked = movies[position].isSelected
        holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
            movies[position].isSelected = !movies[position].isSelected

            (holder.view.context as MainActivity).updateFavorite(movies[position], isChecked)



        }
    }

    override fun getItemCount(): Int = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_table, parent, false)


        return MovieHolder(view)
    }

    fun add(movieList: List<Movie>) {
        movies.clear()
        movies.addAll(movieList)
        notifyDataSetChanged()
    }


}