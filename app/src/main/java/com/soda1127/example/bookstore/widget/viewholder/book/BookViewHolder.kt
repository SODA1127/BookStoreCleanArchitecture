package com.soda1127.example.bookstore.widget.viewholder.book

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.soda1127.example.bookstore.databinding.ViewholderBookBinding
import com.soda1127.example.bookstore.extensions.clear
import com.soda1127.example.bookstore.extensions.load
import com.soda1127.example.bookstore.model.book.BookModel
import com.soda1127.example.bookstore.widget.adapter.listener.books.BooksListener
import com.soda1127.example.bookstore.widget.viewholder.ModelViewHolder

class BookViewHolder(
    private val binding: ViewholderBookBinding,
): ModelViewHolder<BookModel, BooksListener>(binding) {

    override fun reset() = with(binding) {
        bookImageView.clear()
    }

    override fun bindData(model: BookModel) {
        super.bindData(model)
        with(binding) {
            bookImageView.load(model.image, 24f, CenterCrop())
            titleTextView.text = model.title
            subtitleTextView.text = model.subtitle
            priceTextView.text = model.price
        }
    }

    override fun bindViews(model: BookModel, listener: BooksListener) {
        binding.root.setOnClickListener {
            listener.onClickBookItem(model)
        }
    }

}
