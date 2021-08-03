package com.soda1127.example.bookstore.widget.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.soda1127.example.bookstore.model.Model
import com.soda1127.example.bookstore.widget.adapter.listener.AdapterListener

abstract class ModelViewHolder<M: Model, L: AdapterListener>(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun reset()

    open fun bindData(model: M) {
        reset()
    }

    abstract fun bindViews(model: M, listener: L)

}
