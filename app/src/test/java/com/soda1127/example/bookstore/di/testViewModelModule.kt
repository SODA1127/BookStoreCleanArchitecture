package com.soda1127.example.bookstore.di

import com.soda1127.example.bookstore.screen.detail.BookDetailViewModel
import com.soda1127.example.bookstore.screen.main.MainViewModel
import com.soda1127.example.bookstore.screen.main.bookmark.BookmarkTabViewModel
import com.soda1127.example.bookstore.screen.main.search.SearchTabViewModel
import com.soda1127.example.bookstore.screen.main.new.NewTabViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val testViewModelModule = module {

    viewModel { MainViewModel() }

    // Main Tab
    viewModel { NewTabViewModel(get()) }
    viewModel { BookmarkTabViewModel(get()) }
    viewModel { SearchTabViewModel(get(), get()) }

    // Detail
    viewModel { (isbn13: String?) -> BookDetailViewModel(isbn13, get(), get()) }

}
