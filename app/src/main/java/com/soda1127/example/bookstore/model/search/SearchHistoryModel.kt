package com.soda1127.example.bookstore.model.search

import com.soda1127.example.bookstore.model.CellType
import com.soda1127.example.bookstore.model.Model

data class SearchHistoryModel(
    override val id: String,
    override val type: CellType = CellType.SEARCH_HISTORY_CELL,
    val text: String
): Model(id, type)
