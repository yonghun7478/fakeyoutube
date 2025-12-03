package com.example.fakeyoutube.data.source.remote

import com.example.fakeyoutube.data.source.VideoDataSource
import com.example.fakeyoutube.domain.model.VideoEntity
import javax.inject.Inject

/**
 * Real implementation of VideoDataSource for 'prod' build flavor.
 * Will connect to real YouTube API via Retrofit.
 */
class RealVideoDataSource @Inject constructor(
    // TODO: Inject Retrofit Service here
) : VideoDataSource {
    override suspend fun getVideos(): List<VideoEntity> {
        // TODO: Implement actual API call
        throw NotImplementedError("Real API integration not yet implemented")
    }
}