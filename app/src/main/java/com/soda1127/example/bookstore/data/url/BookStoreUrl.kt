package com.soda1127.example.bookstore.data.url

object BookStoreUrl {
    const val BASE_URL = "https://api.itbook.store/1.0/"

    internal object EndPoint {

        const val NEW = "new"

        const val BOOK_ISBN = "books/{isbn13}"

        const val SEARCH = "search/{query}"

        const val SEARCH_PAGE = "search/{query}/{page}"

    }

}
