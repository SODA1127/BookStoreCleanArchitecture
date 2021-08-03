package com.soda1127.example.bookstore.screen.detail

import com.soda1127.example.bookstore.data.entity.BookInfoEntity

sealed class BookDetailState {

    object Uninitialized: BookDetailState()

    object Loading: BookDetailState()

    data class Success(
        val bookInfoEntity: BookInfoEntity,
        val isLiked: Boolean,
        val memo: String
    ): BookDetailState()

    object SaveMemo: BookDetailState()

    sealed class Error: BookDetailState() {

        data class Default(
            val e: Throwable
        ): Error()

        object NotFound: Error()

    }

}
