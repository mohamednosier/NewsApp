package com.tempo.newsapp.api

import com.tempo.newsapp.models.NewsResponse
import com.tempo.newsapp.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country")
        countryCode : String = "eg",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey:String = API_KEY
    ) : Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getSource(
        @Query("sources")
        sources : String = "techcrunch",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey:String = API_KEY
    ) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery : String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey:String = API_KEY
    ) : Response<NewsResponse>
}