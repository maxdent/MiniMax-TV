package com.tvapp.minimax

data class Movie(
    val title: String,
    val description: String,
    val imageUrl: Int,
    val url: String,
    val videoUrl: String? = null
) {
    override fun toString(): String {
        return "Movie(title='$title', description='$description', url='$url')"
    }
}