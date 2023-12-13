package com.example.breakingnewsapp.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArticleViewModel : ViewModel() {
    val selectedArticle = MutableLiveData<Article>()
}