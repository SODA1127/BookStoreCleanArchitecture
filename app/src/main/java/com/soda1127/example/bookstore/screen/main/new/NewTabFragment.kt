package com.soda1127.example.bookstore.screen.main.new

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.soda1127.example.bookstore.R
import com.soda1127.example.bookstore.databinding.FragmentNewTabBinding
import com.soda1127.example.bookstore.delegate.viewBinding
import com.soda1127.example.bookstore.model.Model
import com.soda1127.example.bookstore.model.book.BookModel
import com.soda1127.example.bookstore.screen.base.BaseFragment
import com.soda1127.example.bookstore.screen.base.ScrollableScreen
import com.soda1127.example.bookstore.screen.detail.BookDetailActivity
import com.soda1127.example.bookstore.widget.adapter.ModelRecyclerAdapter
import com.soda1127.example.bookstore.widget.adapter.listener.books.BooksListener
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewTabFragment : BaseFragment<NewTabViewModel, FragmentNewTabBinding>(), ScrollableScreen {

    override val binding: FragmentNewTabBinding by viewBinding()

    override val vm by viewModel<NewTabViewModel>()

    private val adapter by lazy {
        ModelRecyclerAdapter<Model, BooksListener>(object : BooksListener {

            override fun onClickBookItem(model: BookModel) {
                startActivity(
                    BookDetailActivity.newIntent(requireContext(), model.isbn13, model.title)
                )
            }

            override fun onClickLoadRetry() = Unit

        })
    }

    override fun initViews(): Unit = with(binding) {
        recyclerView.adapter = adapter
    }

    override fun observeData() = lifecycleScope.launchWhenStarted {
        vm.newTabStateFlow.collect { state ->
            when (state) {
                is NewTabState.Loading -> handleLoading()
                is NewTabState.Success -> handleSuccess(state)
                is NewTabState.Error -> handleError(state)
                else -> Unit
            }
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.visibility = View.VISIBLE
        errorContainerGroup.visibility = View.GONE
    }

    private fun handleSuccess(state: NewTabState.Success) = with(binding) {
        progressBar.visibility = View.GONE
        adapter.submitList(state.modelList)
    }

    private fun handleError(state: NewTabState.Error) = with(binding) {
        progressBar.visibility = View.GONE
        errorContainerGroup.visibility = View.VISIBLE
        errorMessageTextView.text = getString(R.string.error_occurred, state.e.localizedMessage)
        retryButton.setOnClickListener {
            vm.fetchData()
        }
    }

    override fun scrollUp() {
        binding.recyclerView.smoothScrollToPosition(0)
    }

    companion object {

        fun newInstance(bundle: Bundle = bundleOf()) = NewTabFragment().apply {
            arguments = bundle
        }

        val TAG: String = NewTabFragment::class.simpleName!!

    }

}
