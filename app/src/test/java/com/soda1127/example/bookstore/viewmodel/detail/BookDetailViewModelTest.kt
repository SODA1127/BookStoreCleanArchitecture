package com.soda1127.example.bookstore.viewmodel.detail

import com.google.gson.Gson
import com.soda1127.example.bookstore.data.json.BookInfoResponseJson
import com.soda1127.example.bookstore.data.repository.BookStoreRepository
import com.soda1127.example.bookstore.data.response.BookInfoResponse
import com.soda1127.example.bookstore.screen.detail.BookDetailState
import com.soda1127.example.bookstore.screen.detail.BookDetailViewModel
import com.soda1127.example.bookstore.data.repository.TestBookStoreRepository
import com.soda1127.example.bookstore.viewmodel.ViewModelTest
import dev.olog.flow.test.observer.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
internal class BookDetailViewModelTest: ViewModelTest() {

    private val vm by inject<BookDetailViewModel> { parametersOf(TestBookStoreRepository.TEST_ISBN13) }

    private val bookStoreRepository by inject<BookStoreRepository>()

    private val testMemoText = "testMemoText"
    private val testBookEntity = Gson().fromJson(BookInfoResponseJson, BookInfoResponse::class.java).toEntity()

    @Before
    fun `Insert Test Data`() = runBlockingTest {
        bookStoreRepository.getBookInfo(TestBookStoreRepository.TEST_ISBN13).collect { bookInfo ->
            bookInfo?.let {
                bookStoreRepository.addBookInWishList(it.toBookEntity())
            }
        }
    }

    @Test
    fun `Test Fetch Data`() = runBlockingTest {
        vm.bookDetailStateFlow.test(this) {
            assertValues(
                BookDetailState.Uninitialized,
                BookDetailState.Loading,
                BookDetailState.Success(
                    testBookEntity,
                    true,
                    ""
                )
            )
        }
        vm.fetchData()
    }

    @Test
    fun `Test Toggle Bookmark`() = runBlockingTest {
        vm.bookDetailStateFlow.test(this) {
            assertValues(
                BookDetailState.Uninitialized,
                BookDetailState.Loading,
                BookDetailState.Success(
                    testBookEntity,
                    true,
                    ""
                ),
                BookDetailState.Success(
                    testBookEntity,
                    false,
                    ""
                )
            )
        }
        `Test Fetch Data`()
        vm.toggleLikeButton()
    }

    @Test
    fun `Test Save Memo`() = runBlockingTest {
        vm.bookDetailStateFlow.test(this) {
            assertValues(
                BookDetailState.Uninitialized,
                BookDetailState.Loading,
                BookDetailState.Success(
                    testBookEntity,
                    true,
                    ""
                ),
                BookDetailState.SaveMemo,
                BookDetailState.Loading,
                BookDetailState.Success(
                    testBookEntity,
                    true,
                    testMemoText
                )
            )
        }
        `Test Fetch Data`()
        vm.saveMemo(testMemoText)
        vm.fetchData()
    }

}
