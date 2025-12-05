package com.example.fakeyoutube.domain.usecase

import com.example.fakeyoutube.domain.model.HomeFeedItem
import com.example.fakeyoutube.domain.model.VideoEntity
import com.example.fakeyoutube.domain.repository.HomeRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GetHomeFeedUseCaseTest {

    private val homeRepository = mock(HomeRepository::class.java)
    private val getHomeFeedUseCase = GetHomeFeedUseCase(homeRepository)

    @Test
    fun `invoke should return data from repository`() = runTest {
        // Given
        val mockVideo = VideoEntity(
            id = "1",
            title = "Test Video",
            channelName = "Test Channel",
            views = "100",
            uploadTime = "1 hour ago",
            thumbnailUrl = "url",
            channelImageUrl = "url"
        )
        val mockFeed = listOf(HomeFeedItem.VideoItem(mockVideo))
        `when`(homeRepository.getHomeFeed()).thenReturn(flowOf(mockFeed))

        // When
        val result = getHomeFeedUseCase().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(mockFeed, result.first())
        assertTrue(result.first()[0] is HomeFeedItem.VideoItem)
    }
}