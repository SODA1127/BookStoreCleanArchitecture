package com.soda1127.example.bookstore.viewmodel.main.bookmark

import com.soda1127.example.bookstore.data.entity.BookEntity
import com.soda1127.example.bookstore.data.repository.BookStoreRepository
import com.soda1127.example.bookstore.model.book.BookModel
import com.soda1127.example.bookstore.screen.main.bookmark.BookmarkState
import com.soda1127.example.bookstore.screen.main.bookmark.BookmarkTabViewModel
import com.soda1127.example.bookstore.viewmodel.ViewModelTest
import com.soda1127.example.bookstore.data.repository.TestBookStoreRepository
import dev.olog.flow.test.observer.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.flow.collect
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
internal class BookmarkTabViewModelTest : ViewModelTest() {

    private val bookStoreRepository by inject<BookStoreRepository>()

    private val vm by inject<BookmarkTabViewModel>()

    private val wishList = mutableListOf<BookEntity>()

    private lateinit var bookModel: BookModel

    @Before
    fun `Insert Test Data`() = runBlockingTest {
        bookStoreRepository.getBookInfo(TestBookStoreRepository.TEST_ISBN13).collect { bookInfo ->
            bookInfo?.let { book ->
                bookStoreRepository.addBookInWishList(book.toBookEntity())
                wishList.add(book.toBookEntity())
                bookModel = BookModel(
                    id = book.isbn13,
                    title = book.title,
                    subtitle = book.subtitle,
                    isbn13 = book.isbn13,
                    price = book.price,
                    image = book.image,
                    url = book.url,
                    isLiked = true
                )
            }
        }
    }

    @Test
    fun `Test data exist in wish list`() = runBlockingTest {
        vm.bookmarkStateFlow.test(this) {
            assertValues(
                BookmarkState.Uninitialized,
                BookmarkState.Success(listOf()),
                BookmarkState.Loading,
                BookmarkState.Success(
                    wishList.map { book ->
                        BookModel(
                            id = book.isbn13,
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
        vm.fetchData()
    }

    @Test
    fun `Test Book Model toggle like button`() = runBlockingTest {
        vm.bookmarkStateFlow.test(this) {
            assertValues(
                BookmarkState.Uninitialized,
                BookmarkState.Success(listOf()),
                BookmarkState.Loading,
                BookmarkState.Success(listOf(bookModel)),
                BookmarkState.Success(listOf(bookModel.copy(isLiked = false))),
            )
        }
        `Test data exist in wish list`()
        vm.toggleLikeButton(bookModel)
    }

}
