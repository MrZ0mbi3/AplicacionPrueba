package com.example.aplicacionprueba.controller

import com.example.aplicacionprueba.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface MdbService {
    @GET
    suspend fun getAllMoviesNames (@Url url: String): Response<MdbResponse>

    @GET
    suspend fun getTokenAuthentication(@Url url: String): Response<MdbResponseAuthenticationToken>

    @POST
    suspend fun createSession(@Url url:String, @Body user:UserAuth)

    @POST
    suspend fun getSessionId(@Url url: String, @Body token:TokenRequested): Response<MdbResponseSessionId>

    @GET
    suspend fun getFavoriteMovies(@Url url: String): Response<MdbResponse>

    @POST
    suspend fun setFavoriteMovie(@Url url:String, @Body favoriteMovie: FavoriteMovieMessage)
}