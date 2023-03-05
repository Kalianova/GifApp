package ru.kalianova.gifapp

import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataService {
    @GET("gifs/trending")
    fun getGifs(
        @Query("api_key") api_key: String,
        @Query("offset") offset: Int
    ): retrofit2.Call<GifDataResult>

    @GET("gifs/search")
    fun searchGifs(
        @Query("api_key") api_key: String,
        @Query("q") query: String
    ): retrofit2.Call<GifDataResult>
}