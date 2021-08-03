package com.soda1127.example.bookstore.viewmodel.main

import com.soda1127.example.bookstore.R
import com.soda1127.example.bookstore.screen.main.MainNavigation
import com.soda1127.example.bookstore.screen.main.MainViewModel
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
internal class MainViewModelTest: ViewModelTest() {

    private val vm by inject<MainViewModel>()

    @Test
    fun `Test main tab navigation changed`() = runBlockingTest {
        val first = MainNavigation(R.id.menu_new)
        val second = MainNavigation(R.id.menu_bookmark)
        vm.navigationItemStateFlow.test(this) {
            assertValues(
                null,
                first,
                second
            )
        }
        vm.changeNavigation(first)
        vm.changeNavigation(second)
    }

}
