package com.soda1127.example.bookstore.screen.detail

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.soda1127.example.bookstore.R
import com.soda1127.example.bookstore.data.entity.BookInfoEntity
import com.soda1127.example.bookstore.databinding.ActivityBookDetailBinding
import com.soda1127.example.bookstore.delegate.viewBinding
import com.soda1127.example.bookstore.extensions.load
import com.soda1127.example.bookstore.screen.base.BaseActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.full.memberProperties

class BookDetailActivity : BaseActivity<BookDetailViewModel, ActivityBookDetailBinding>() {

    override val vm by viewModel<BookDetailViewModel> {
        parametersOf(
            intent.getStringExtra(KEY_ISBN13)
        )
    }

    override val binding: ActivityBookDetailBinding by viewBinding()

    override fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener { onBackPressed() }
        titleTextView.text = intent.getStringExtra(KEY_TITLE)

        likedButton.visibility = View.GONE
    }

    override fun observeData(): Job = lifecycleScope.launchWhenStarted {
        vm.bookDetailStateFlow.collect { state ->
            when (state) {
                is BookDetailState.Loading -> handleLoading()
                is BookDetailState.Success -> handleSuccess(state)
                is BookDetailState.Error -> handleError(state)
                is BookDetailState.SaveMemo -> handleSaveMemo()
                else -> Unit
            }
        }
    }

    private fun handleLoading() = with(binding) {
        errorContainerGroup.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun handleSuccess(state: BookDetailState.Success) = with(binding) {
        progressBar.visibility = View.GONE
        scrollView.visibility = View.VISIBLE
        likedButton.visibility = View.VISIBLE

        likedButton.setOnClickListener {
            vm.toggleLikeButton()
        }

        likedButton.setImageDrawable(
            ContextCompat.getDrawable(
                this@BookDetailActivity, if (state.isLiked) {
                    R.drawable.ic_heart_enable
                } else {
                    R.drawable.ic_heart_disable
                }
            )
        )

        val bookInfoEntity = state.bookInfoEntity

        bookImageView.load(bookInfoEntity.image)

        bookMemoInput.setText(state.memo)

        var infoText = ""
        BookInfoEntity::class.memberProperties.forEach { property ->
            infoText += "[${property.name}] : ${property.get(bookInfoEntity)}\n\n"
        }


        bookInfoTextView.text = infoText
    }

    private fun handleError(state: BookDetailState.Error) = with(binding) {
        when (state) {
            is BookDetailState.Error.Default -> {
                scrollView.visibility = View.GONE
                progressBar.visibility = View.GONE
                errorContainerGroup.visibility = View.VISIBLE
                errorMessageTextView.text = getString(R.string.error_occurred, state.e.localizedMessage)
                retryButton.setOnClickListener {
                    vm.fetchData()
                }
            }
            is BookDetailState.Error.NotFound -> {
                Toast.makeText(this@BookDetailActivity, "책정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun handleSaveMemo() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        vm.saveMemo(binding.bookMemoInput.text.toString())
    }

    companion object {

        const val KEY_ISBN13 = "KEY_ISBN13"
        const val KEY_TITLE = "KEY_TITLE"

        fun newIntent(context: Context, isbn13: String, title: String) =
            Intent(context, BookDetailActivity::class.java).apply {
                putExtra(KEY_ISBN13, isbn13)
                putExtra(KEY_TITLE, title)
            }

    }

}
