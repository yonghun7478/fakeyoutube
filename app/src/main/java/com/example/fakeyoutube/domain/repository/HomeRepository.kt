package com.example.fakeyoutube.domain.repository

import com.example.fakeyoutube.domain.model.HomeFeedItem
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHomeFeed(): Flow<List<HomeFeedItem>>
}