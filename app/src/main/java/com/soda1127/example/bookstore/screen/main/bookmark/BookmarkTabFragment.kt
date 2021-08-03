package com.soda1127.example.bookstore.screen.main.bookmark

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SimpleItemAnimator
import com.soda1127.example.bookstore.R
import com.soda1127.example.bookstore.databinding.FragmentBookmarkTabBinding
import com.soda1127.example.bookstore.delegate.viewBinding
import com.soda1127.example.bookstore.model.Model
import com.soda1127.example.bookstore.model.book.BookModel
import com.soda1127.example.bookstore.screen.base.BaseFragment
import com.soda1127.example.bookstore.screen.base.ScrollableScreen
import com.soda1127.example.bookstore.screen.detail.BookDetailActivity
import com.soda1127.example.bookstore.screen.main.MainNavigation
import com.soda1127.example.bookstore.screen.main.MainViewModel
import com.soda1127.example.bookstore.widget.adapter.ModelRecyclerAdapter
import com.soda1127.example.bookstore.widget.adapter.listener.books.BookmarkListener
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookmarkTabFragment: BaseFragment<BookmarkTabViewModel, FragmentBookmarkTabBinding>(), ScrollableScreen {

    override val binding: FragmentBookmarkTabBinding by viewBinding()

    override val vm by viewModel<BookmarkTabViewModel>()

    private val mainViewModel by sharedViewModel<MainViewModel>()

    private val adapter by lazy {
        ModelRecyclerAdapter<Model, BookmarkListener>(object : BookmarkListener {

            override fun onClickBookItem(model: BookModel) {
                startActivity(
                    BookDetailActivity.newIntent(requireContext(), model.isbn13, model.title)
                )
            }

            override fun onClickLikedButton(model: BookModel) {
                vm.toggleLikeButton(model)
            }

            override fun onClickLoadRetry() = Unit

        })
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        emptyButton.setOnClickListener {
            mainViewModel.changeNavigation(MainNavigation(R.id.menu_new))
        }
    }

    override fun observeData() = lifecycleScope.launchWhenStarted {
        vm.bookmarkStateFlow.collect { state ->
            when (state) {
                is BookmarkState.Loading -> handleLoading()
                is BookmarkState.Success -> handleSuccess(state)
                else -> Unit
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vm.fetchData()
    }

    private fun handleLoading() = with(binding) {
        progressBar.visibility = View.VISIBLE
    }

    private fun handleSuccess(state: BookmarkState.Success) = with(binding) {
        progressBar.visibility = View.GONE
        val modelList = state.modelList
        if (modelList.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyButton.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyButton.visibility = View.GONE
            adapter.submitList(modelList)
        }
    }

    override fun scrollUp() {
        binding.recyclerView.smoothScrollToPosition(0)
    }

    companion object {

        fun newInstance(bundle: Bundle = bundleOf()) = BookmarkTabFragment().apply {
            arguments = bundle
        }

        val TAG : String = BookmarkTabFragment::class.simpleName!!

    }

}
