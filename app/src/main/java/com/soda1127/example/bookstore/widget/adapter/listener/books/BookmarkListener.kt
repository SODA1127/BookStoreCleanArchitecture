package com.soda1127.example.bookstore.widget.adapter.listener.books

import com.soda1127.example.bookstore.model.book.BookModel

interface BookmarkListener: BooksListener {

    override fun onClickBookItem(model: BookModel)

    fun onClickLikedButton(model: BookModel)

}
