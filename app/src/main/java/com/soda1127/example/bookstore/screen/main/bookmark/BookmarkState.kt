package com.soda1127.example.bookstore.screen.main.bookmark

import com.soda1127.example.bookstore.model.book.BookModel

sealed class BookmarkState {

    object Uninitialized: BookmarkState()

    object Loading: BookmarkState()

    data class Success(
        val modelList: List<BookModel>
    ): BookmarkState()

}
