package com.soda1127.example.bookstore.di

import com.soda1127.example.bookstore.data.repository.*
import org.koin.dsl.module

val repositoryModule = module {

    single<BookStoreRepository> { DefaultBookStoreRepository(get(), get(), get()) }

    single<BookMemoRepository> { DefaultBookMemoRepository(get(), get()) }

    single<SearchRepository> { DefaultSearchRepository(get(), get()) }

}
