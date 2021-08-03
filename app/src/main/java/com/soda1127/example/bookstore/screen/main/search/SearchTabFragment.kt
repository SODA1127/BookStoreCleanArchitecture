package com.soda1127.example.bookstore.screen.main.search

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.soda1127.example.bookstore.R
import com.soda1127.example.bookstore.databinding.FragmentSearchTabBinding
import com.soda1127.example.bookstore.delegate.viewBinding
import com.soda1127.example.bookstore.extensions.hideSoftInput
import com.soda1127.example.bookstore.model.Model
import com.soda1127.example.bookstore.model.book.BookModel
import com.soda1127.example.bookstore.screen.base.BaseFragment
import com.soda1127.example.bookstore.screen.base.ScrollableScreen
import com.soda1127.example.bookstore.screen.detail.BookDetailActivity
import com.soda1127.example.bookstore.widget.adapter.ModelRecyclerAdapter
import com.soda1127.example.bookstore.widget.adapter.listener.books.BooksListener
import com.soda1127.example.bookstore.widget.adapter.listener.search.SearchHistoryListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchTabFragment: BaseFragment<SearchTabViewModel, FragmentSearchTabBinding>(), ScrollableScreen {

    override val binding: FragmentSearchTabBinding by viewBinding()

    override val vm by viewModel<SearchTabViewModel>()

    private val searchResultAdapter by lazy {
        ModelRecyclerAdapter<Model, BooksListener>(object : BooksListener {

            override fun onClickBookItem(model: BookModel) {
                startActivity(
                    BookDetailActivity.newIntent(requireContext(), model.isbn13, model.title)
                )
            }

            override fun onClickLoadRetry() {
                vm.loadMoreSearchResult(isLoadRetry = true)
            }

        })
    }

    private val searchHistoryAdapter by lazy {
        ModelRecyclerAdapter<Model, SearchHistoryListener>(object : SearchHistoryListener {

            override fun onClickSearchHistoryItem(keyword: String) {
                vm.searchByHistory(keyword)
            }

            override fun onClickRemoveItem(keyword: String) {
                vm.removeHistory(keyword)
            }

        })
    }

    override fun initViews(): Unit = with(binding) {
        searchResultRecyclerView.adapter = searchResultAdapter
        searchHistoryRecyclerView.adapter = searchHistoryAdapter
        searchButton.setOnClickListener {
            vm.searchByKeyword(searchInput.text.toString())
            binding.root.hideSoftInput()
        }
        searchResultRecyclerView.setOnScrollChangeListener { _: View?, _: Int, _: Int, _: Int, _: Int -> nextPageRender() }
        searchInput.addTextChangedListener {
            if (it.toString().isEmpty()) {
                vm.fetchData()
            }
        }
    }

    private fun nextPageRender() {
        val lastVisibleItemPosition =
            (binding.searchResultRecyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
        val itemTotalCount = binding.searchResultRecyclerView.adapter!!.itemCount - 1
        if (itemTotalCount <= lastVisibleItemPosition) {
            vm.loadMoreSearchResult()
        }
    }

    override fun observeData() = lifecycleScope.launchWhenStarted {
        vm.searchTabStateFlow.collect { state ->
            when (state) {
                is SearchTabState.Loading -> handleLoading()
                is SearchTabState.Success.SearchResult -> handleSearchResult(state)
                is SearchTabState.Success.SearchHistory -> handleSearchHistory(state)
                is SearchTabState.Error -> handleError(state)
                else -> Unit
            }
        }
    }

    private fun handleLoading() = with(binding) {
        errorContainerGroup.visibility = View.GONE
        emptyTextView.visibility = View.GONE
        searchResultRecyclerView.visibility = View.GONE
        searchHistoryRecyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun handleSearchResult(state: SearchTabState.Success.SearchResult) = with(binding) {
        searchInput.setText(state.searchKeyword ?: "")
        searchInput.setSelection(searchInput.text.toString().length)
        searchResultRecyclerView.visibility = View.VISIBLE
        searchHistoryRecyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE

        val modelList = state.modelList
        if (modelList.isNotEmpty()) {
            emptyTextView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.VISIBLE
            emptyTextView.setText(R.string.empty_search_result)
        }
        searchResultAdapter.submitList(modelList)
    }

    private fun handleSearchHistory(state: SearchTabState.Success.SearchHistory) = with(binding) {
        searchResultRecyclerView.visibility = View.GONE
        searchHistoryRecyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        val modelList = state.modelList
        if (modelList.isNotEmpty()) {
            emptyTextView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.VISIBLE
            emptyTextView.setText(R.string.empty_search_history)
        }
        searchHistoryAdapter.submitList(state.modelList)

        lifecycleScope.launch {
            delay(200)
            searchHistoryRecyclerView.scrollToPosition(0)
        }
    }

    private fun handleError(state: SearchTabState.Error) = with(binding) {
        progressBar.visibility = View.GONE
        errorContainerGroup.visibility = View.VISIBLE
        errorMessageTextView.text = getString(R.string.error_occurred, state.e.localizedMessage)
        retryButton.setOnClickListener {
            state.searchKeyword?.let { searchKeyword ->
                vm.searchByKeyword(searchKeyword)
            } ?: vm.fetchData()
        }
    }

    override fun scrollUp() {
        binding.searchResultRecyclerView.smoothScrollToPosition(0)
    }

    companion object {

        fun newInstance(bundle: Bundle = bundleOf()) = SearchTabFragment().apply {
            arguments = bundle
        }

        val TAG : String = SearchTabFragment::class.simpleName!!

    }

}
