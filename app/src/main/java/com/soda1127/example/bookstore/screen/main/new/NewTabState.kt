package com.soda1127.example.bookstore.screen.main.new

import com.soda1127.example.bookstore.model.book.BookModel

sealed class NewTabState {

    object Uninitialized: NewTabState()

    object Loading: NewTabState()

    data class Success(
        val modelList: List<BookModel>
    ): NewTabState()

    data class Error(
        val e: Throwable
    ): NewTabState()

}
