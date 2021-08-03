package com.soda1127.example.bookstore.model.book

import com.soda1127.example.bookstore.model.CellType
import com.soda1127.example.bookstore.model.Model

data class BookLoadingModel(
    override val id: String,
    override val type: CellType = CellType.BOOK_LOADING_CELL,
): Model(id, type)
