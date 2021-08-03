package com.soda1127.example.bookstore.screen.main.search

import androidx.lifecycle.viewModelScope
import com.soda1127.example.bookstore.data.entity.SearchHistoryEntity
import com.soda1127.example.bookstore.data.repository.BookStoreRepository
import com.soda1127.example.bookstore.data.repository.SearchRepository
import com.soda1127.example.bookstore.model.book.BookLoadRetryModel
import com.soda1127.example.bookstore.model.book.BookLoadingModel
import com.soda1127.example.bookstore.model.book.BookModel
import com.soda1127.example.bookstore.model.search.SearchHistoryModel
import com.soda1127.example.bookstore.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class SearchTabViewModel(
    private val bookStoreRepository: BookStoreRepository,
    private val searchRepository: SearchRepository
) : BaseViewModel() {

    private val _historyTabStateFlow = MutableStateFlow<SearchTabState>(SearchTabState.Uninitialized)
    val searchTabStateFlow: StateFlow<SearchTabState> = _historyTabStateFlow

    override fun fetchData(): Job = viewModelScope.launch {
        setState(
            SearchTabState.Loading
        )
        try {
            searchRepository.getAllSearchHistories().collect { searchHistories ->
                setState(
                    SearchTabState.Success.SearchHistory(
                        searchHistories.sortedByDescending { it.searchTimestamp }.map {
                            SearchHistoryModel(
                                id = it.searchKeyword,
                                text = it.searchKeyword
                            )
                        }
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            setState(
                SearchTabState.Error(e)
            )
        }
    }

    fun searchByHistory(keyword: String) = viewModelScope.launch {
        searchRepository.getSearchHistory(keyword).collect { searchHistory ->
            searchHistory?.let {
                searchRepository.deleteSearchHistory(searchHistory.searchKeyword)
                searchByKeyword(searchHistory.searchKeyword)
            }
        }
    }

    fun removeHistory(keyword: String) = viewModelScope.launch {
        try {
            searchRepository.deleteSearchHistory(keyword)
            searchRepository.getAllSearchHistories().collect { searchHistories ->
                setState(
                    SearchTabState.Success.SearchHistory(
                        searchHistories.sortedByDescending { it.searchTimestamp }.map {
                            SearchHistoryModel(
                                id = it.searchKeyword,
                                text = it.searchKeyword
                            )
                        }
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            setState(
                SearchTabState.Error(e)
            )
        }
    }

    fun searchByKeyword(keyword: String) = viewModelScope.launch {
        try {
            setState(
                SearchTabState.Loading
            )
            searchRepository.saveSearchHistory(
                SearchHistoryEntity(
                    searchKeyword = keyword,
                    searchTimestamp = Date().time
                )
            )
            bookStoreRepository.searchBooksByKeyword(keyword).collect { (bookList, page, total) ->
                setState(
                    SearchTabState.Success.SearchResult(
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
                        keyword,
                        page,
                        total
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            setState(
                SearchTabState.Error(e, keyword)
            )
        }
    }

    private var isLoading: Boolean = false
    private var isErrorOccurred: Boolean = false
    fun loadMoreSearchResult(isLoadRetry: Boolean = false) = viewModelScope.launch {
        try {
            if (isLoadRetry) {
                isErrorOccurred = false
            }
            if (isLoading || isErrorOccurred) return@launch
            when (val data = searchTabStateFlow.value) {
                is SearchTabState.Success.SearchResult -> {
                    if (data.totalResultCount == 0) return@launch

                    /**
                     * 데이터 로드 요청 중 상태라면 표현
                     * 이 때, 기존 Retry Button이 보함 될 수 있으므로 마지막에 있는지 체크 후 로딩 추가
                     */
                    setState(
                        data.copy(
                            modelList = data.modelList.toMutableList().apply {
                                val lastModel = last()
                                if (lastModel is BookLoadRetryModel && lastModel.id == "RetryButton") {
                                    remove(lastModel)
                                }
                                add(BookLoadingModel(id = "loading"))
                            }
                        )
                    )

                    data.searchKeyword?.let { keyword ->
                        val modelList = data.modelList.toMutableList()
                        isLoading = true
                        bookStoreRepository.searchBooksByKeyword(
                            keyword,
                            data.currentPage?.toInt()?.plus(1).toString()
                        ).collect { (bookList, page, total) ->

                            val appendModelList = bookList.map { book ->
                                BookModel(
                                    id = book.isbn13,
                                    title = book.title,
                                    subtitle = book.subtitle,
                                    isbn13 = book.isbn13,
                                    price = book.price,
                                    image = book.image,
                                    url = book.url
                                )
                            }

                            setState(
                                data.copy(
                                    modelList = modelList.apply {
                                        val lastModel = last()
                                        if (lastModel is BookLoadingModel && lastModel.id == "loading") {
                                            remove(lastModel)
                                        }
                                        if (lastModel is BookLoadRetryModel && lastModel.id == "RetryButton") {
                                            remove(lastModel)
                                        }
                                        addAll(appendModelList)
                                    },
                                    currentPage = page,
                                    totalResultCount = total
                                )
                            )
                            isLoading = false
                            isErrorOccurred = false
                        }
                    }
                }
            }
        } catch (e: Exception) {
            if (isErrorOccurred) return@launch
            when (val data = searchTabStateFlow.value) {
                is SearchTabState.Success.SearchResult -> {
                    val modelList = data.modelList.toMutableList()
                    setState(
                        data.copy(
                            modelList = modelList.apply {
                                val lastModel = last()
                                if (lastModel is BookLoadingModel && lastModel.id == "loading") {
                                    remove(lastModel)
                                }
                                if (filterIsInstance<BookLoadRetryModel>().isEmpty()) {
                                    add(
                                        BookLoadRetryModel(
                                            id = "RetryButton",
                                            errorMessage = e.localizedMessage
                                        )
                                    )
                                }
                            },
                        )
                    )
                }
            }
            isErrorOccurred = true
            isLoading = false
        }
    }

    private fun setState(state: SearchTabState) {
        _historyTabStateFlow.value = state
    }

}
