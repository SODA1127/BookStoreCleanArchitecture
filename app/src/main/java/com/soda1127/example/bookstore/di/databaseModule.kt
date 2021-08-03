package com.soda1127.example.bookstore.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    single { provideDB(androidApplication()) }
    single { provideBookInWishListDao(get()) }
    single { provideBookMemoDao(get()) }
    single { provideSearchHistoryDao(get()) }

}
