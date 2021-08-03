package com.soda1127.example.bookstore.model.book

import com.soda1127.example.bookstore.model.CellType
import com.soda1127.example.bookstore.model.Model

data class BookLoadRetryModel(
    override val id: String,
    override val type: CellType = CellType.BOOK_LOAD_RETRY_CELL,
    val errorMessage: String? = null,
) : Model(id, type)
