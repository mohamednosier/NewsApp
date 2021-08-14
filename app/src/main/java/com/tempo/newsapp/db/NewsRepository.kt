package com.tempo.newsapp.db

import com.tempo.newsapp.api.RetrofitInstance
import com.tempo.newsapp.models.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val articleDao: ArticleDao
) {

    suspend fun getNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getNews(countryCode, pageNumber)

    suspend fun getSearchResults(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun getSource(sources: String, pageNumber: Int) =
        RetrofitInstance.api.getSource(sources, pageNumber)




}