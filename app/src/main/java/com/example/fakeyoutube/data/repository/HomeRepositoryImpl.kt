package com.example.fakeyoutube.data.repository

import com.example.fakeyoutube.data.datasource.LocalDummyDataSource
import com.example.fakeyoutube.domain.model.HomeFeedItem
import com.example.fakeyoutube.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val localDummyDataSource: LocalDummyDataSource
) : HomeRepository {

    override fun getHomeFeed(): Flow<List<HomeFeedItem>> = flow {
        val shorts = localDummyDataSource.getShorts()
        val videos = localDummyDataSource.getVideos()

        val feedItems = mutableListOf<HomeFeedItem>()
        
        // Add Shorts Section first (as per typical layout, or after first video)
        // Here we add it at the top for simplicity as per requirement implies structure
        if (shorts.isNotEmpty()) {
            feedItems.add(HomeFeedItem.ShortsSectionItem(shorts))
        }

        // Add Videos
        feedItems.addAll(videos.map { HomeFeedItem.VideoItem(it) })

        emit(feedItems)
    }
}