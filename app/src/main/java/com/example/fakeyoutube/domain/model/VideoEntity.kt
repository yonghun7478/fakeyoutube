package com.example.fakeyoutube.domain.model

data class VideoEntity(
    val id: String,
    val title: String,
    val channelName: String,
    val views: String,
    val uploadTime: String,
    val thumbnailUrl: String,
    val channelImageUrl: String
)