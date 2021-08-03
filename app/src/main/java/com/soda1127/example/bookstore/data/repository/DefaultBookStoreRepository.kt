package com.soda1127.example.bookstore.data.repository

import com.soda1127.example.bookstore.data.db.dao.BookInWishListDao
import com.soda1127.example.bookstore.data.entity.BookEntity
import com.soda1127.example.bookstore.data.entity.BookInfoEntity
import com.soda1127.example.bookstore.data.network.BooksApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class DefaultBookStoreRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val booksApiService: BooksApiService,
    private val bookInWishListDao: BookInWishListDao
): BookStoreRepository {

    override suspend fun getNewBooks(): Flow<List<BookEntity>> = withContext(ioDispatcher) {
        flow<List<BookEntity>> {
            val booksResponse = booksApiService.getNewBooks()
            if (booksResponse.isSuccessful) {
                emit(booksResponse.body()?.books ?: listOf())
            } else {
                emit(listOf())
            }

        }
    }

    override suspend fun getBookInfo(isbn13: String): Flow<BookInfoEntity?> = withContext(ioDispatcher) {
        flow {
            val bookInfoResponse = booksApiService.getBookInfo(isbn13)
            if (bookInfoResponse.isSuccessful) {
                emit(bookInfoResponse.body()?.toEntity())
            } else {
                emit(null)
            }
        }
    }

    override suspend fun getBooksInWishList(): Flow<List<BookEntity>> = withContext(ioDispatcher) {
        flow {
            emit(bookInWishListDao.getAll())
        }
    }

    override suspend fun getBookInWishList(isbn13: String): Flow<BookEntity?> = withContext(ioDispatcher) {
        flow {
            emit(bookInWishListDao.get(isbn13))
        }
    }

    override suspend fun addBookInWishList(bookEntity: BookEntity) = withContext(ioDispatcher) {
        bookInWishListDao.insert(bookEntity)
    }

    override suspend fun removeBookInWishList(isbn13: String) = withContext(ioDispatcher) {
        bookInWishListDao.delete(isbn13)
    }

    override suspend fun searchBooksByKeyword(keyword: String, page: String?): Flow<Triple<List<BookEntity>, String?, Int?>> = withContext(ioDispatcher) {
        val response = when (page) {
            null -> {
                booksApiService.searchBooksByKeyword(keyword)
            }
            else -> {
                booksApiService.searchBooksByKeywordWithPage(keyword, page)
            }
        }
        flow<Triple<List<BookEntity>, String?, Int?>> {
            if (response.isSuccessful) {
                val body = response.body()
                emit(Triple(body?.books ?: listOf(), body?.page, body?.total?.toInt()))
            } else {
                emit(Triple(listOf(), null, null))
            }
        }
    }

}
