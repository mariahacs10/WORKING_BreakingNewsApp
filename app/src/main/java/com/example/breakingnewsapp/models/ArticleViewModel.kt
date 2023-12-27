package com.example.breakingnewsapp.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ArticleViewModel(private val repository: ArticleRepository) : ViewModel() {

    // Selected article for UI state
    val selectedArticle = MutableLiveData<Article>()

    // Get saved articles from repository
    private val _savedArticles = repository.getSavedArticles()

    // Expose as read-only livedata
    val savedArticles: LiveData<List<Article>> = _savedArticles

    // Launch coroutine to call repository
    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.insertArticle(article)
    }

}