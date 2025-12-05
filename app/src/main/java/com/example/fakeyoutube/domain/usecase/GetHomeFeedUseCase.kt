package com.example.fakeyoutube.domain.usecase

import com.example.fakeyoutube.domain.model.HomeFeedItem
import com.example.fakeyoutube.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeFeedUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<List<HomeFeedItem>> {
        return homeRepository.getHomeFeed()
    }
}