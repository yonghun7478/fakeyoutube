package com.example.fakeyoutube.domain.model

sealed interface HomeFeedItem {
    data class ShortsSectionItem(val shorts: List<ShortsEntity>) : HomeFeedItem
    data class VideoItem(val video: VideoEntity) : HomeFeedItem
}