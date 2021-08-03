package com.soda1127.example.bookstore.data.repository

import com.soda1127.example.bookstore.data.entity.BookMemoEntity
import kotlinx.coroutines.flow.Flow

interface BookMemoRepository {

    suspend fun getBookMemo(isbn13: String): Flow<BookMemoEntity?>

    suspend fun saveBookMemo(bookMemoEntity: BookMemoEntity)

}
