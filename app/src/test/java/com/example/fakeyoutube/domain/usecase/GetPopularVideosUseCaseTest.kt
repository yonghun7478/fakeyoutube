package com.example.fakeyoutube.domain.usecase

import com.example.fakeyoutube.domain.model.Video
import com.example.fakeyoutube.domain.repository.VideoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetPopularVideosUseCaseTest {

    @Mock
    private lateinit var videoRepository: VideoRepository

    private lateinit var getPopularVideosUseCase: GetPopularVideosUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getPopularVideosUseCase = GetPopularVideosUseCase(videoRepository)
    }

    @Test
    fun `invoke returns list of videos from repository`() = runBlocking {
        // Given
        val mockVideos = listOf(
            Video(
                id = "1",
                title = "Test Video",
                description = "Desc",
                thumbnailUrl = "thumb.jpg",
                videoUrl = "video.mp4",
                channelTitle = "Channel",
                viewCount = 100,
                publishedAt = "2023-01-01"
            )
        )
        Mockito.`when`(videoRepository.getPopularVideos()).thenReturn(flowOf(mockVideos))

        // When
        val result = getPopularVideosUseCase().first()

        // Then
        assertEquals(mockVideos, result)
        Mockito.verify(videoRepository).getPopularVideos()
    }
}