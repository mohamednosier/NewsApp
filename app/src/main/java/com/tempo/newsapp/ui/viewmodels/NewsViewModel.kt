package com.tempo.newsapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.tempo.newsapp.db.NewsRepository
import com.tempo.newsapp.models.NewsResponse
import com.tempo.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel @ViewModelInject constructor(private val newsRepository: NewsRepository) : ViewModel() {
    val news: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var newsPage = 1
    var newsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    val sourcesNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var sourcesPage = 1
    var sourcesResponse: NewsResponse? = null

    init {
        getNews("eg")
    }

     fun getNews(countryCode: String) {
        viewModelScope.launch {
            news.postValue(Resource.Loading())
            val response =
                newsRepository.getNews(countryCode, newsPage)
            news.postValue(handleNewsResponse(response))

        }
    }

    fun searchNews(searchQuery: String) {
        viewModelScope.launch {
            searchNews.postValue(Resource.Loading())
            val response =
                newsRepository.getSearchResults(searchQuery, searchNewsPage)
            searchNews.postValue(handleSearchNewsResponse(response))
        }
    }
    fun getSource(sources: String) {
        viewModelScope.launch {
            sourcesNews.postValue(Resource.Loading())
            val response =
                newsRepository.getSource(sources, sourcesPage)
            sourcesNews.postValue(handleSourcesNewsResponse(response))
        }
    }

    private fun handleNewsResponse(
        response: Response<NewsResponse>
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                newsPage++
                if(newsResponse == null){
                    newsResponse = it
                } else{
                    val oldArticles = newsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(newsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSourcesNewsResponse(
        response: Response<NewsResponse>
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                sourcesPage++
                if(sourcesResponse == null){
                    sourcesResponse = it
                } else{
                    val oldArticles = sourcesResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(sourcesResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchNewsResponse(
        response: Response<NewsResponse>
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                searchNewsPage++
                if(searchNewsResponse == null){
                    searchNewsResponse = it
                } else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }


}