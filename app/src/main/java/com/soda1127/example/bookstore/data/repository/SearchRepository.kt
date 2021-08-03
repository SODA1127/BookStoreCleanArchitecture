package com.soda1127.example.bookstore.data.repository

import com.soda1127.example.bookstore.data.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun saveSearchHistory(searchHistoryEntity: SearchHistoryEntity)

    suspend fun getAllSearchHistories(): Flow<List<SearchHistoryEntity>>

    suspend fun getSearchHistory(keyword: String): Flow<SearchHistoryEntity?>

    suspend fun deleteSearchHistory(keyword: String)

}
