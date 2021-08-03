package com.soda1127.example.bookstore.data.response

import com.soda1127.example.bookstore.data.entity.BookEntity

/**
{
"title":"TypeScript Notes for Professionals",
"subtitle":"",
"isbn13":"1001622115721",
"price":"$0.00",
"image":"https://itbook.store/img/books/1001622115721.png",
"url":"https://itbook.store/books/1001622115721"
},
 */

data class BookStoreNewResponse(
    val error: String,
    val total: String,
    val books: List<BookEntity>
)
