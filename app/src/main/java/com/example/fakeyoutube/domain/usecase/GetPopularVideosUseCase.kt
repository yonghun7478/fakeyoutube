package com.example.fakeyoutube.domain.usecase

import com.example.fakeyoutube.domain.model.Video
import com.example.fakeyoutube.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * UseCase to retrieve popular videos.
 * Encapsulates the business logic for fetching the home feed videos.
 */
class GetPopularVideosUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    operator fun invoke(): Flow<List<Video>> {
        return videoRepository.getPopularVideos()
    }
}