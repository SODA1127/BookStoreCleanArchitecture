package com.soda1127.example.bookstore.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soda1127.example.bookstore.data.db.dao.BookInWishListDao
import com.soda1127.example.bookstore.data.db.dao.BookMemoDao
import com.soda1127.example.bookstore.data.db.dao.SearchHistoryDao
import com.soda1127.example.bookstore.data.entity.BookEntity
import com.soda1127.example.bookstore.data.entity.BookMemoEntity
import com.soda1127.example.bookstore.data.entity.SearchHistoryEntity

@Database(
    entities = [BookEntity::class, BookMemoEntity::class, SearchHistoryEntity::class],
    version = 1,
    exportSchema = false
)

abstract class BookStoreDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "BookStoreDatabase.db"
    }

    abstract fun BookInWishListDao(): BookInWishListDao
    abstract fun BookMemoDao(): BookMemoDao
    abstract fun SearchHistoryDao(): SearchHistoryDao

}
