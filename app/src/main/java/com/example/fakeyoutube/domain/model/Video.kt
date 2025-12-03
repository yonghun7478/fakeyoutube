package com.example.fakeyoutube.domain.model

/**
 * Video Entity representing a video item in the domain layer.
 */
data class Video(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val channelTitle: String,
    val viewCount: Long,
    val publishedAt: String
)