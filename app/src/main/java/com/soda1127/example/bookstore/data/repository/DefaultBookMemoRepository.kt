package com.soda1127.example.bookstore.data.repository

import com.soda1127.example.bookstore.data.db.dao.BookMemoDao
import com.soda1127.example.bookstore.data.entity.BookMemoEntity
import com.soda1127.example.bookstore.data.repository.BookMemoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class DefaultBookMemoRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val bookMemoDao: BookMemoDao
): BookMemoRepository {

    override suspend fun getBookMemo(isbn13: String): Flow<BookMemoEntity?> = withContext(ioDispatcher) {
        flow {
            emit(bookMemoDao.get(isbn13))
        }
    }

    override suspend fun saveBookMemo(bookMemoEntity: BookMemoEntity) = withContext(ioDispatcher) {
        bookMemoDao.insert(bookMemoEntity)
    }

}
