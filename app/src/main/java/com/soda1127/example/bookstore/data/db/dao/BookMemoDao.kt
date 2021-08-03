package com.soda1127.example.bookstore.data.db.dao

import androidx.room.*
import com.soda1127.example.bookstore.data.entity.BookMemoEntity

@Dao
interface BookMemoDao {

    @Query("SELECT * FROM BookMemoEntity")
    suspend fun getAll(): List<BookMemoEntity>

    @Query("SELECT * FROM BookMemoEntity WHERE isbn13=:isbn13")
    suspend fun get(isbn13: String): BookMemoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(BookMemoEntity: BookMemoEntity)

    @Query("DELETE FROM BookMemoEntity WHERE isbn13=:isbn13")
    suspend fun delete(isbn13: String)

    @Query("DELETE FROM BookMemoEntity")
    suspend fun deleteAll()

}
