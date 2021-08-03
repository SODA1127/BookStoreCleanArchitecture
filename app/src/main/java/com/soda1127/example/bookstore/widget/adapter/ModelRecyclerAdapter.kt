package com.soda1127.example.bookstore.widget.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soda1127.example.bookstore.model.CellType
import com.soda1127.example.bookstore.model.Model
import com.soda1127.example.bookstore.widget.adapter.listener.AdapterListener
import com.soda1127.example.bookstore.widget.adapter.mapper.ModelViewHolderMapper
import com.soda1127.example.bookstore.widget.viewholder.ModelViewHolder

class ModelRecyclerAdapter<M : Model, L : AdapterListener>(
    private val adapterListener: L
) : ListAdapter<Model, ModelViewHolder<M, L>>(Model.DIFF_CALLBACK) {

    override fun getItemViewType(position: Int) = getItem(position).type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M, L> {
        return ModelViewHolderMapper.map(parent, CellType.values()[viewType])
    }

    override fun onBindViewHolder(holder: ModelViewHolder<M, L>, position: Int) {
        val safePosition = holder.adapterPosition
        if (safePosition != RecyclerView.NO_POSITION) {
            @Suppress("UNCHECKED_CAST")
            val model = getItem(position) as M
            with(holder) {
                bindData(model)
                bindViews(model, adapterListener)
            }
        }
    }

}
