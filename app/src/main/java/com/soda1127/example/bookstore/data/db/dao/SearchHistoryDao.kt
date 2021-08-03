package com.soda1127.example.bookstore.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soda1127.example.bookstore.data.entity.SearchHistoryEntity

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM SearchHistoryEntity WHERE searchKeyword=:keyword")
    suspend fun get(keyword: String): SearchHistoryEntity?

    @Query("SELECT * FROM SearchHistoryEntity")
    suspend fun getAll(): List<SearchHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(SearchHistoryEntity: SearchHistoryEntity)

    @Query("DELETE FROM SearchHistoryEntity WHERE searchKeyword=:keyword")
    suspend fun delete(keyword: String)

    @Query("DELETE FROM SearchHistoryEntity")
    suspend fun deleteAll()

}
