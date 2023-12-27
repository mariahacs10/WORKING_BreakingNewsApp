package com.example.breakingnewsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**Entity annotation in Room indicates that this data class
 * defines a table in the database.*/
@Entity(
    tableName = "article"
)
data class Article(
    // Primary key will auto generate unique IDs
    //(if any)
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    // Fields mapped to table columns
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,

    // Local flag for saved state
    var isSaved: Boolean = false
) : Serializable


