package com.soda1127.example.bookstore.widget.viewholder.search

import com.soda1127.example.bookstore.databinding.ViewholderSearchHistoryBinding
import com.soda1127.example.bookstore.model.search.SearchHistoryModel
import com.soda1127.example.bookstore.widget.adapter.listener.search.SearchHistoryListener
import com.soda1127.example.bookstore.widget.viewholder.ModelViewHolder

class SearchHistoryViewHolder(
    private val binding: ViewholderSearchHistoryBinding
): ModelViewHolder<SearchHistoryModel, SearchHistoryListener>(binding) {

    override fun reset() = Unit

    override fun bindData(model: SearchHistoryModel) {
        super.bindData(model)
        binding.searchHistoryTextView.text = model.text
    }

    override fun bindViews(model: SearchHistoryModel, listener: SearchHistoryListener) {
        binding.root.setOnClickListener {
            listener.onClickSearchHistoryItem(model.text)
        }
        binding.removeImageView.setOnClickListener {
            listener.onClickRemoveItem(model.text)
        }
    }


}
