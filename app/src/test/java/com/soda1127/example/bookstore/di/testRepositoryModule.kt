package com.soda1127.example.bookstore.di

import com.soda1127.example.bookstore.data.repository.*
import org.koin.dsl.module

val testRepositoryModule = module {

    single<BookStoreRepository> { TestBookStoreRepository() }

    single<BookMemoRepository> { TestBookMemoRepository() }

    single<SearchRepository> { TestSearchRepository() }

}
