package com.example.fakeyoutube.data.datasource

import com.example.fakeyoutube.domain.model.ShortsEntity
import com.example.fakeyoutube.domain.model.VideoEntity
import javax.inject.Inject

class LocalDummyDataSource @Inject constructor() {

    fun getVideos(): List<VideoEntity> {
        return listOf(
            VideoEntity(
                id = "v1",
                title = "Android Jetpack Compose Tutorial - Part 1",
                channelName = "Android Developers",
                views = "1.2M views",
                uploadTime = "2 years ago",
                thumbnailUrl = "https://picsum.photos/seed/v1/640/360",
                channelImageUrl = "https://picsum.photos/seed/c1/100/100"
            ),
            VideoEntity(
                id = "v2",
                title = "Amazing Nature 4K",
                channelName = "Nature Channel",
                views = "500K views",
                uploadTime = "1 month ago",
                thumbnailUrl = "https://picsum.photos/seed/v2/640/360",
                channelImageUrl = "https://picsum.photos/seed/c2/100/100"
            ),
            VideoEntity(
                id = "v3",
                title = "Coding in Kotlin is Fun",
                channelName = "Kotlin Official",
                views = "300K views",
                uploadTime = "3 weeks ago",
                thumbnailUrl = "https://picsum.photos/seed/v3/640/360",
                channelImageUrl = "https://picsum.photos/seed/c3/100/100"
            ),
            VideoEntity(
                id = "v4",
                title = "Fake YouTube App Building Process",
                channelName = "Dev Builder",
                views = "10K views",
                uploadTime = "1 day ago",
                thumbnailUrl = "https://picsum.photos/seed/v4/640/360",
                channelImageUrl = "https://picsum.photos/seed/c4/100/100"
            ),
            VideoEntity(
                id = "v5",
                title = "Top 10 Travel Destinations 2024",
                channelName = "Travel Guide",
                views = "2.5M views",
                uploadTime = "5 months ago",
                thumbnailUrl = "https://picsum.photos/seed/v5/640/360",
                channelImageUrl = "https://picsum.photos/seed/c5/100/100"
            )
        )
    }

    fun getShorts(): List<ShortsEntity> {
        return listOf(
            ShortsEntity(
                id = "s1",
                title = "Funny Cat Moments",
                views = "2M views",
                thumbnailUrl = "https://picsum.photos/seed/s1/300/500"
            ),
            ShortsEntity(
                id = "s2",
                title = "Quick Recipe: Pasta",
                views = "500K views",
                thumbnailUrl = "https://picsum.photos/seed/s2/300/500"
            ),
            ShortsEntity(
                id = "s3",
                title = "Life Hacks #12",
                views = "1.5M views",
                thumbnailUrl = "https://picsum.photos/seed/s3/300/500"
            ),
            ShortsEntity(
                id = "s4",
                title = "Dance Challenge",
                views = "3M views",
                thumbnailUrl = "https://picsum.photos/seed/s4/300/500"
            ),
            ShortsEntity(
                id = "s5",
                title = "DIY Crafts",
                views = "800K views",
                thumbnailUrl = "https://picsum.photos/seed/s5/300/500"
            )
        )
    }
}