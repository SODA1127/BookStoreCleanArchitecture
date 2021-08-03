package com.soda1127.example.bookstore.data.response

import com.soda1127.example.bookstore.data.entity.BookEntity

/**
{
    "error":"0",
    "total":"1533",
    "page":"1",
    "books":[
        {
            "title":"Oracle PL/SQL Language Pocket Reference, 4th Edition",
            "subtitle":"A Guide to Oracle's PL/SQL Language Fundamentals",
            "isbn13":"9780596514044",
            "price":"$5.98",
            "image":"https://itbook.store/img/books/9780596514044.png",
            "url":"https://itbook.store/books/9780596514044"
        },
    ]
}
 */

data class BookSearchResultResponse(
    val error: String,
    val total: String,
    val page: String,
    val books: List<BookEntity>
)
