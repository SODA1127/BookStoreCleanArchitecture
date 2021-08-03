package com.soda1127.example.bookstore.screen.main.new

import androidx.lifecycle.viewModelScope
import com.soda1127.example.bookstore.screen.base.BaseViewModel
import com.soda1127.example.bookstore.data.repository.BookStoreRepository
import com.soda1127.example.bookstore.model.book.BookModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewTabViewModel(
    private val bookStoreRepository: BookStoreRepository
) : BaseViewModel() {

    private val _newTabStateFlow = MutableStateFlow<NewTabState>(NewTabState.Uninitialized)
    val newTabStateFlow: StateFlow<NewTabState> = _newTabStateFlow

    override fun fetchData(): Job = viewModelScope.launch {
        setState(
            NewTabState.Loading
        )
        try {
            bookStoreRepository.getNewBooks().collect { bookList ->
                setState(
                    NewTabState.Success(
                        bookList.map { book ->
                            BookModel(
                                id = book.isbn13,
                                title = book.title,
                                subtitle = book.subtitle,
                                isbn13 = book.isbn13,
                                price = book.price,
                                image = book.image,
                                url = book.url
                            )
                        },
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            setState(
                NewTabState.Error(e)
            )
        }
    }

    private fun setState(state: NewTabState) {
        _newTabStateFlow.value = state
    }

}
