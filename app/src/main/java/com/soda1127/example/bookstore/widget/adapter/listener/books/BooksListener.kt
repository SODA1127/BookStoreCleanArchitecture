package com.soda1127.example.bookstore.widget.adapter.listener.books

import com.soda1127.example.bookstore.model.book.BookModel
import com.soda1127.example.bookstore.widget.adapter.listener.AdapterListener

interface BooksListener: AdapterListener {

    fun onClickBookItem(model: BookModel)

    fun onClickLoadRetry()

}
