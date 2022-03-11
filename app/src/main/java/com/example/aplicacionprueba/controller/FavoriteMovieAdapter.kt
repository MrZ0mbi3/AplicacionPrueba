package com.example.aplicacionprueba.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionprueba.R
import com.example.aplicacionprueba.model.Movie

class FavoriteMovieAdapter (val movies: MutableList<Movie>) : RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteHolder>() {
    class FavoriteHolder (val view: View): RecyclerView.ViewHolder(view){
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.textMovieNameFavorite)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_item_table,parent, false)

        return FavoriteHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.textView.text = movies[position].title
    }

    override fun getItemCount(): Int = movies.size

    fun add(favMovieList: List<Movie>) {
        movies.clear()
        movies.addAll(favMovieList)
        notifyDataSetChanged()
    }
}