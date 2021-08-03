package com.soda1127.example.bookstore.model.book

import com.soda1127.example.bookstore.data.entity.BookEntity
import com.soda1127.example.bookstore.model.CellType
import com.soda1127.example.bookstore.model.Model

data class BookModel(
    override val id: String,
    override val type: CellType = CellType.BOOK_CELL,
    val title: String,
    val subtitle: String,
    val isbn13: String,
    val price: String,
    val image: String,
    val url: String,
    val isLiked: Boolean? = null
) : Model(id, type) {

    fun toEntity() = BookEntity(
        title,
        subtitle,
        isbn13,
        price,
        image,
        url
    )

}
