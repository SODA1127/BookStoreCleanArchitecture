package com.soda1127.example.bookstore.data.repository

import com.soda1127.example.bookstore.data.entity.SearchHistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TestSearchRepository: SearchRepository {

    private val searchHistoryList = mutableListOf<SearchHistoryEntity>()

    override suspend fun saveSearchHistory(searchHistoryEntity: SearchHistoryEntity) = withContext(Dispatchers.Main) {
        searchHistoryList.add(searchHistoryEntity)
        return@withContext
    }

    override suspend fun getAllSearchHistories(): Flow<List<SearchHistoryEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchHistory(keyword: String): Flow<SearchHistoryEntity?> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSearchHistory(keyword: String) {
        TODO("Not yet implemented")
    }

}
