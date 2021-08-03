package com.soda1127.example.bookstore.viewmodel.main.new

import com.google.gson.Gson
import com.soda1127.example.bookstore.data.json.NewBooksResponseJson
import com.soda1127.example.bookstore.data.response.BookStoreNewResponse
import com.soda1127.example.bookstore.model.book.BookModel
import com.soda1127.example.bookstore.screen.main.new.NewTabState
import com.soda1127.example.bookstore.screen.main.new.NewTabViewModel
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
internal class NewTabViewModelTest: ViewModelTest() {

    private val vm by inject<NewTabViewModel>()

    @Test
    fun `Test fetch Book List`() = runBlockingTest {
        vm.newTabStateFlow.test(this) {
            assertValues(
                NewTabState.Uninitialized,
                NewTabState.Loading,
                NewTabState.Success(
                    Gson().fromJson(NewBooksResponseJson, BookStoreNewResponse::class.java).books.map { book ->
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
            )
        }
        vm.fetchData()
    }
    
}
