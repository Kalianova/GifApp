package ru.kalianova.gifapp

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataService {

    @GET("gifs/trending")
    suspend fun getGifs(
        @Query("api_key") api_key: String,
        @Query("offset") offset: Int
    ): Response<GifDataResult>

    @GET("gifs/search")
    suspend fun searchGifs(
        @Query("api_key") api_key: String,
        @Query("offset") offset: Int,
        @Query("q") query: String
    ): Response<GifDataResult>

    companion object {
        const val BASE_URL = "https://api.giphy.com/v1/"
        operator fun invoke(): GetDataService =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build().create(GetDataService::class.java)
    }
}