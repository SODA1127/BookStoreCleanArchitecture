package com.soda1127.example.bookstore.data.db.dao

import androidx.room.*
import com.soda1127.example.bookstore.data.entity.BookEntity

@Dao
interface BookInWishListDao {

    @Query("SELECT * FROM BookEntity")
    suspend fun getAll(): List<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE isbn13=:isbn13")
    suspend fun get(isbn13: String): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(BookEntity: BookEntity)

    @Query("DELETE FROM BookEntity WHERE isbn13=:isbn13")
    suspend fun delete(isbn13: String)

    @Query("DELETE FROM BookEntity")
    suspend fun deleteAll()

}
