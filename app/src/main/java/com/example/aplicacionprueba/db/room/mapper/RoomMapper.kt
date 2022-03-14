package com.example.aplicacionprueba.db.room.mapper


import com.example.aplicacionprueba.db.room.entity.MovieEntity
import com.example.aplicacionprueba.db.room.entity.UserEntity
import com.example.aplicacionprueba.model.Movie
import com.example.aplicacionprueba.model.User

fun User.toUserEntity() : UserEntity {
    return UserEntity (userName)
}
fun UserEntity.toUser(favoriteMovies: MutableSet<Movie>) : User {
    return User(userName,favoriteMovies )
}

fun Movie.toMovieEntity(nameUser:String) : MovieEntity {
    return MovieEntity (adult,backdrop_path,id,original_language,original_title,overview,popularity,poster_path,release_date,title,video,vote_average,vote_count,isSelected,nameUser)
}

fun MovieEntity.toMovie(nameUser:String) : Movie{
    return Movie(adult,backdrop_path, listOf(),id,original_language,original_title,overview,popularity,poster_path,release_date,title,video,vote_average,vote_count)
}