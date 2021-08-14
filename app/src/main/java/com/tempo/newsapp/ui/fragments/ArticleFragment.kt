package com.tempo.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tempo.newsapp.R
import com.tempo.newsapp.models.Article
import com.tempo.newsapp.ui.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_article.*

@AndroidEntryPoint
class ArticleFragment : Fragment(R.layout.fragment_article) {
    private val viewModel: NewsViewModel by viewModels()
    private val args:ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        setupView(article)



    }

    private fun setupView(article: Article){
        Glide.with(this).load(article.urlToImage).into(ArticleImage)
        ArticleTitle.text = article.title
        ArticleDescription.text = article.description
        ArticleAuthor.text=article.author
        ArticlePublished.text="Published at "+article.publishedAt
        ArticleContent.text=article.content
        source.text= "ٍٍٍSource : "+article.source?.name
        tvSource.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }
            findNavController().navigate(
                R.id.action_sourceNewsFragment_to_sourceFragment,
                bundle
            )
        }
    }

}