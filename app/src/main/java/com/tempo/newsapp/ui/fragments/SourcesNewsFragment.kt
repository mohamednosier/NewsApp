package com.tempo.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tempo.newsapp.R
import com.tempo.newsapp.adapters.SourceAdapter
import com.tempo.newsapp.models.Article
import com.tempo.newsapp.ui.viewmodels.NewsViewModel
import com.tempo.newsapp.util.Constants.QUERY_PAGE_SIZE
import com.tempo.newsapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sources_news.*


@AndroidEntryPoint
class SourcesNewsFragment : Fragment(R.layout.fragment_sources_news) {


    private val viewModel: NewsViewModel by viewModels()
    private val args:SourcesNewsFragmentArgs by navArgs()
    lateinit var sourceAdapter: SourceAdapter
    private val TAG = "Sources NEWS FRAGMENT"
    lateinit var article1: Article
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()
        if(args.article != null ){
            article1 = args.article
            viewModel.getSource(article1.source?.id.toString())
        }else{
            //set default value
            viewModel.getSource("google-news")
        }
        viewModel.sourcesNews.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        sourceAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.sourcesPage == totalPages
                        if (isLastPage) {
                            rvsourcesNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, message)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

            }
        })

    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem &&
                    isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getSource(article1.source?.id.toString())
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        sourceAdapter = SourceAdapter()
        rvsourcesNews.apply {
            adapter = sourceAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SourcesNewsFragment.scrollListener)
        }
    }
}