package com.example.fakeyoutube.domain.repository

import com.example.fakeyoutube.domain.model.Video
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Video data operations.
 * Follows Clean Architecture: Domain layer defines the interface, Data layer implements it.
 */
interface VideoRepository {
    fun getPopularVideos(): Flow<List<Video>>
}