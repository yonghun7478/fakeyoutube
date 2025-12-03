package com.example.fakeyoutube.data.source.mock

import com.example.fakeyoutube.data.source.VideoDataSource
import com.example.fakeyoutube.domain.model.VideoEntity
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Mock implementation of VideoDataSource for 'mock' build flavor.
 * Returns hardcoded fake data.
 */
class MockVideoDataSource @Inject constructor() : VideoDataSource {
    override suspend fun getVideos(): List<VideoEntity> {
        // Simulate network delay
        delay(500)
        
        return listOf(
            VideoEntity(
                id = "1",
                title = "Mock Video 1",
                description = "This is a description for mock video 1",
                thumbnailUrl = "https://via.placeholder.com/150",
                channelTitle = "Mock Channel A",
                viewCount = "1.2M",
                publishedAt = "2 days ago"
            ),
            VideoEntity(
                id = "2",
                title = "Mock Video 2",
                description = "This is a description for mock video 2",
                thumbnailUrl = "https://via.placeholder.com/150",
                channelTitle = "Mock Channel B",
                viewCount = "500K",
                publishedAt = "1 week ago"
            ),
            VideoEntity(
                id = "3",
                title = "Mock Video 3",
                description = "This is a description for mock video 3",
                thumbnailUrl = "https://via.placeholder.com/150",
                channelTitle = "Mock Channel C",
                viewCount = "23K",
                publishedAt = "5 hours ago"
            )
        )
    }
}