package com.soda1127.example.bookstore.data.repository

import com.soda1127.example.bookstore.data.entity.BookMemoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TestBookMemoRepository: BookMemoRepository {

    private val bookMemoList = mutableListOf<BookMemoEntity>()

    override suspend fun getBookMemo(isbn13: String): Flow<BookMemoEntity?> = withContext(Dispatchers.Main) {
        flow {
            emit(bookMemoList.find { it.isbn13 == isbn13 })
        }
    }

    override suspend fun saveBookMemo(bookMemoEntity: BookMemoEntity) = withContext(Dispatchers.Main) {
        bookMemoList.add(bookMemoEntity)
        return@withContext
    }

}
