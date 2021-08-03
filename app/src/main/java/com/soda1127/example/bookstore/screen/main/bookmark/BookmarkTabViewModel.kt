package com.soda1127.example.bookstore.screen.main.bookmark

import androidx.lifecycle.viewModelScope
import com.soda1127.example.bookstore.data.repository.BookStoreRepository
import com.soda1127.example.bookstore.model.CellType
import com.soda1127.example.bookstore.model.book.BookModel
import com.soda1127.example.bookstore.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookmarkTabViewModel(
    private val bookStoreRepository: BookStoreRepository
): BaseViewModel() {

    private val _bookmarkStateFlow = MutableStateFlow<BookmarkState>(BookmarkState.Uninitialized)
    val bookmarkStateFlow: StateFlow<BookmarkState> = _bookmarkStateFlow

    override fun fetchData(): Job = viewModelScope.launch {
        setState(
            BookmarkState.Success(listOf())
        )
        setState(
            BookmarkState.Loading
        )
        bookStoreRepository.getBooksInWishList().collect { bookList ->
            setState(
                BookmarkState.Success(
                    bookList.map { book ->
                        BookModel(
                            id = book.isbn13,
                            type = CellType.BOOKMARK_CELL,
                            title = book.title,
                            subtitle = book.subtitle,
                            isbn13 = book.isbn13,
                            price = book.price,
                            image = book.image,
                            url = book.url,
                            isLiked = true
                        )
                    },
                )
            )
        }
    }

    fun toggleLikeButton(bookModel: BookModel) = viewModelScope.launch {
        try {
            when (val data = bookmarkStateFlow.value) {
                is BookmarkState.Success -> {
                    if (bookModel.isLiked == true) {
                        bookStoreRepository.removeBookInWishList(bookModel.isbn13)
                    } else {
                        bookStoreRepository.addBookInWishList(bookModel.toEntity())
                    }
                    setState(
                        data.copy(
                            modelList = data.modelList.toMutableList().apply {
                                set(
                                    this.indexOf(bookModel),
                                    bookModel.copy(
                                        isLiked = bookModel.isLiked?.not()
                                    )
                                )
                            }.toList()
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setState(state: BookmarkState) {
        _bookmarkStateFlow.value = state
    }

}
