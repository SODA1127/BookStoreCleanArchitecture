package com.soda1127.example.bookstore.viewmodel.main.search

import com.google.gson.Gson
import com.soda1127.example.bookstore.data.json.BookSearchResponseJsonFirstPage
import com.soda1127.example.bookstore.data.response.BookSearchResultResponse
import com.soda1127.example.bookstore.model.book.BookModel
import com.soda1127.example.bookstore.screen.main.search.SearchTabState
import com.soda1127.example.bookstore.screen.main.search.SearchTabViewModel
import com.soda1127.example.bookstore.data.repository.TestBookStoreRepository
import com.soda1127.example.bookstore.viewmodel.ViewModelTest
import dev.olog.flow.test.observer.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.test.inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
internal class SearchTabViewModelTest : ViewModelTest() {

    private val vm by inject<SearchTabViewModel>()

    private val firstSearchResult = Gson().fromJson(BookSearchResponseJsonFirstPage, BookSearchResultResponse::class.java)

    private val searchKeyword = TestBookStoreRepository.TEST_KEYWORD

    private val searchResultModelList = mutableListOf<BookModel>()

    @Test
    fun `Test Search Result`() = runBlockingTest {
        vm.searchTabStateFlow.test(this) {
            searchResultModelList.addAll(
                firstSearchResult.books.map { book ->
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
            )
            assertValues(
                SearchTabState.Uninitialized,
                SearchTabState.Success.SearchResult(
                    listOf()
                ),
                SearchTabState.Loading,
                SearchTabState.Success.SearchResult(
                    searchResultModelList,
                    searchKeyword,
                    firstSearchResult.page,
                    firstSearchResult.total.toInt()
                )
            )

        }
        vm.searchByKeyword(searchKeyword)
    }

}
