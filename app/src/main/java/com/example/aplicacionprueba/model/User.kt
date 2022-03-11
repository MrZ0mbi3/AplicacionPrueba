package com.example.aplicacionprueba.model

data class User (val userName:String, private val favoriteMovies:MutableSet<Movie>)
{
    @Synchronized
    fun addFavoriteMovie(movie: Movie){
        favoriteMovies.add(movie)
    }

    @Synchronized
    fun removeFavoriteMovie(movie: Movie) {
        favoriteMovies.remove(movie)
    }

    @Synchronized
    fun getFavoriteMovies(): List<Movie> {
        return favoriteMovies.toList()
    }

    fun addAll(movies: MutableList<Movie>) {
        favoriteMovies.addAll(movies)
    }
}