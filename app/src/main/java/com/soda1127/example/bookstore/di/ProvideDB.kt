package com.soda1127.example.bookstore.di

import android.content.Context
import androidx.room.Room
import com.soda1127.example.bookstore.data.db.BookStoreDatabase

fun provideDB(context: Context): BookStoreDatabase =
    Room.databaseBuilder(context, BookStoreDatabase::class.java, BookStoreDatabase.DB_NAME).build()

fun provideBookInWishListDao(database: BookStoreDatabase) = database.BookInWishListDao()

fun provideBookMemoDao(database: BookStoreDatabase) = database.BookMemoDao()

fun provideSearchHistoryDao(database: BookStoreDatabase) = database.SearchHistoryDao()

