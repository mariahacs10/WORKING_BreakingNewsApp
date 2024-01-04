package com.example.breakingnewsapp.api

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.breakingnewsapp.models.Article

@Dao
interface ArticleDao {

    // Annotation marks insert method to database
    @Insert
    suspend fun insert(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    // Query annotation defines SQL statement
    @Query("SELECT * FROM article")

    // Map query result to LiveData
    fun getAllArticles(): LiveData<List<Article>>

}