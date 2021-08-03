package com.soda1127.example.bookstore.di

import org.koin.dsl.module

val networkModule = module {

    single { provideGsonConverterFactory() }
    single { buildOkHttpClient() }

    single { provideBookStoreRetrofit(get(), get()) }

    single { provideBooksApiService(get()) }

}
