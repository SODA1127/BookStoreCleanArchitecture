package com.soda1127.example.bookstore.screen.detail

import androidx.lifecycle.viewModelScope
import com.soda1127.example.bookstore.data.entity.BookMemoEntity
import com.soda1127.example.bookstore.data.repository.BookMemoRepository
import com.soda1127.example.bookstore.data.repository.BookStoreRepository
import com.soda1127.example.bookstore.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val isbn13: String?,
    private val bookStoreRepository: BookStoreRepository,
    private val bookMemoRepository: BookMemoRepository
) : BaseViewModel() {

    private val _bookDetailStateFlow = MutableStateFlow<BookDetailState>(BookDetailState.Uninitialized)
    val bookDetailStateFlow: StateFlow<BookDetailState> = _bookDetailStateFlow

    override fun fetchData(): Job = viewModelScope.launch {
        try {
            setState(
                BookDetailState.Loading
            )
            isbn13?.let { isbn13 ->
                combine(
                    bookStoreRepository.getBookInWishList(isbn13),
                    bookStoreRepository.getBookInfo(isbn13),
                    bookMemoRepository.getBookMemo(isbn13)
                ) { bookInfoEntityInWishList, bookInfoEntity, bookMemoEntity ->
                    Triple(bookInfoEntityInWishList, bookInfoEntity, bookMemoEntity)
                }.collect { (bookInfoEntityInWishList, bookInfoEntity, bookMemoEntity) ->
                    if (bookInfoEntity != null) {
                        setState(
                            BookDetailState.Success(
                                bookInfoEntity,
                                bookInfoEntityInWishList != null,
                                bookMemoEntity?.memo ?: ""
                            )
                        )
                    } else {
                        setState(
                            BookDetailState.Error.NotFound
                        )
                    }
                }
            } ?: kotlin.run {
                setState(
                    BookDetailState.Error.NotFound
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            setState(
                BookDetailState.Error.Default(e)
            )
        }
    }

    fun toggleLikeButton() = viewModelScope.launch {
        try {
            when (val data = bookDetailStateFlow.value) {
                is BookDetailState.Success -> {
                    if (data.isLiked) {
                        bookStoreRepository.removeBookInWishList(data.bookInfoEntity.isbn13)
                    } else {
                        bookStoreRepository.addBookInWishList(data.bookInfoEntity.toBookEntity())
                    }
                    setState(
                        data.copy(
                            isLiked = data.isLiked.not()
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            setState(
                BookDetailState.Error.Default(e)
            )
        }
    }

    fun saveMemo(memo: String) = viewModelScope.launch {
        isbn13?.let { isbn13 ->
            bookMemoRepository.saveBookMemo(
                BookMemoEntity(
                    isbn13,
                    memo
                )
            )
            setState(
                BookDetailState.SaveMemo
            )
        } ?: kotlin.run {
            setState(
                BookDetailState.SaveMemo
            )
        }
    }

    private fun setState(state: BookDetailState) {
        _bookDetailStateFlow.value = state
    }

}
