package com.soda1127.example.bookstore.data.network

import com.soda1127.example.bookstore.data.response.BookInfoResponse
import com.soda1127.example.bookstore.data.response.BookSearchResultResponse
import com.soda1127.example.bookstore.data.response.BookStoreNewResponse
import com.soda1127.example.bookstore.data.url.BookStoreUrl
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BooksApiService {

    @GET(BookStoreUrl.EndPoint.NEW)
    suspend fun getNewBooks(): Response<BookStoreNewResponse>

    @GET(BookStoreUrl.EndPoint.BOOK_ISBN)
    suspend fun getBookInfo(@Path("isbn13") isbn13: String): Response<BookInfoResponse>

    @GET(BookStoreUrl.EndPoint.SEARCH)
    suspend fun searchBooksByKeyword(@Path("query") keyword: String): Response<BookSearchResultResponse>

    @GET(BookStoreUrl.EndPoint.SEARCH_PAGE)
    suspend fun searchBooksByKeywordWithPage(@Path("query") keyword: String, @Path("page") page: String): Response<BookSearchResultResponse>

}
