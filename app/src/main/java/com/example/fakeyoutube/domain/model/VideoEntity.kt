package com.example.fakeyoutube.domain.model

/**
 * Domain layer Video Entity
 */
data class VideoEntity(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val channelTitle: String,
    val viewCount: String,
    val publishedAt: String
)