package com.example.fakeyoutube.data.source

import com.example.fakeyoutube.domain.model.VideoEntity

/**
 * Interface for Video Data Source
 * Defines the contract for fetching video data.
 */
interface VideoDataSource {
    suspend fun getVideos(): List<VideoEntity>
}