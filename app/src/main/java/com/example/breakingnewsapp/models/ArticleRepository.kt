package com.example.breakingnewsapp.models

import android.content.Context
import com.example.breakingnewsapp.api.ArticleDao

class ArticleRepository(context: Context) {

    //This accesses the database
    private val db = AppDatabase.getDatabase(context)
    // Access DAO from the database
    private val articleDao = db.articleDao()

    // Query all articles through DAO
    fun getSavedArticles() = articleDao.getAllArticles()

    // Call insert on DAO
    suspend fun insertArticle(article: Article) = articleDao.insert(article)

}
