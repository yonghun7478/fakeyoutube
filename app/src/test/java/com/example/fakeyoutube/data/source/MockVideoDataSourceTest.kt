package com.example.fakeyoutube.data.source

import com.example.fakeyoutube.data.source.mock.MockVideoDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MockVideoDataSourceTest {

    @Test
    fun `getVideos returns list of mock videos`() = runTest {
        // Given
        val dataSource = MockVideoDataSource()

        // When
        val videos = dataSource.getVideos()

        // Then
        assertTrue(videos.isNotEmpty())
        assertEquals("Mock Video 1", videos[0].title)
        assertEquals("1", videos[0].id)
    }
}