package com.soda1127.example.bookstore.data.repository

import com.google.gson.Gson
import com.soda1127.example.bookstore.data.entity.BookEntity
import com.soda1127.example.bookstore.data.entity.BookInfoEntity
import com.soda1127.example.bookstore.data.json.NewBooksResponseJson
import com.soda1127.example.bookstore.data.json.BookInfoResponseJson
import com.soda1127.example.bookstore.data.json.BookSearchResponseJsonFirstPage
import com.soda1127.example.bookstore.data.response.BookInfoResponse
import com.soda1127.example.bookstore.data.response.BookSearchResultResponse
import com.soda1127.example.bookstore.data.response.BookStoreNewResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TestBookStoreRepository : BookStoreRepository {

    companion object {
        const val TEST_ISBN13 = "1001622115721"

        const val TEST_KEYWORD = "kotlin"
    }

    private val booksInWishList = mutableListOf<BookEntity>()

    override suspend fun getNewBooks(): Flow<List<BookEntity>> = withContext(Dispatchers.Main) {
        flow<List<BookEntity>> {
            emit(Gson().fromJson(NewBooksResponseJson, BookStoreNewResponse::class.java).books)
        }
    }

    override suspend fun getBookInfo(isbn13: String): Flow<BookInfoEntity?> = withContext(Dispatchers.Main) {
        flow {
            if (isbn13 == TEST_ISBN13) {
                emit(Gson().fromJson(BookInfoResponseJson, BookInfoResponse::class.java).toEntity())
            } else {
                emit(null)
            }
        }
    }

    override suspend fun getBooksInWishList(): Flow<List<BookEntity>> = withContext(Dispatchers.Main) {
        flow {
            emit(booksInWishList)
        }
    }

    override suspend fun getBookInWishList(isbn13: String): Flow<BookEntity?> = withContext(Dispatchers.Main) {
        flow {
            emit(booksInWishList.find { it.isbn13 == isbn13 })
        }
    }

    override suspend fun addBookInWishList(bookEntity: BookEntity) {
        booksInWishList.add(bookEntity)
    }

    override suspend fun removeBookInWishList(isbn13: String) = withContext(Dispatchers.Main) {
        booksInWishList.remove(booksInWishList.find { it.isbn13 == isbn13 })
        return@withContext
    }

    override suspend fun searchBooksByKeyword(keyword: String, page: String?): Flow<Triple<List<BookEntity>, String?, Int?>> = withContext(Dispatchers.Main) {
        val response = when {
            keyword == TEST_KEYWORD && page == null -> {
                Gson().fromJson(BookSearchResponseJsonFirstPage, BookSearchResultResponse::class.java)
            }
            else -> null
        }
        flow<Triple<List<BookEntity>, String?, Int?>> {
            if (response != null) {
                emit(Triple(response.books, response.page, response.total.toInt()))
            } else {
                emit(Triple(listOf(), null, null))
            }
        }
    }

}
