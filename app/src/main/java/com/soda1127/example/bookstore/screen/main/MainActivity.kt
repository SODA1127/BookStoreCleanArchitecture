package com.soda1127.example.bookstore.screen.main

import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.soda1127.example.bookstore.R
import com.soda1127.example.bookstore.databinding.ActivityMainBinding
import com.soda1127.example.bookstore.delegate.viewBinding
import com.soda1127.example.bookstore.extensions.hideSoftInput
import com.soda1127.example.bookstore.screen.base.BaseActivity
import com.soda1127.example.bookstore.screen.base.ScrollableScreen
import com.soda1127.example.bookstore.screen.main.bookmark.BookmarkTabFragment
import com.soda1127.example.bookstore.screen.main.search.SearchTabFragment
import com.soda1127.example.bookstore.screen.main.new.NewTabFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), BottomNavigationView.OnNavigationItemSelectedListener {

    override val binding: ActivityMainBinding by viewBinding()

    override val vm by viewModel<MainViewModel>()

    override fun initState() {
        super.initState()
        vm.changeNavigation(MainNavigation(R.id.menu_new))
    }

    override fun initViews() = with(binding) {
        bottomNav.setOnNavigationItemSelectedListener(this@MainActivity)
    }

    override fun observeData() = lifecycleScope.launch {
        vm.navigationItemStateFlow.collect { navigation ->
            navigation ?: return@collect
            binding.bottomNav.selectedItemId = navigation.navigationMenuId
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navigationId = item.itemId
        if (binding.bottomNav.selectedItemId == navigationId) {
            scrollTop(navigationId)
        }
        return when (navigationId) {
            R.id.menu_new -> {
                showFragment(NewTabFragment.TAG)
                true
            }
            R.id.menu_bookmark -> {
                showFragment(BookmarkTabFragment.TAG)
                true
            }
            R.id.menu_history -> {
                showFragment(SearchTabFragment.TAG)
                true
            }
            else -> false
        }
    }

    private fun scrollTop(navigationId: Int) =
        when(navigationId) {
            R.id.menu_new -> scrollFragmentTop(NewTabFragment.TAG)
            R.id.menu_bookmark -> scrollFragmentTop(BookmarkTabFragment.TAG)
            R.id.menu_history -> scrollFragmentTop(SearchTabFragment.TAG)
            else -> Unit
        }

    private fun scrollFragmentTop(tag: String) {
        binding.root.hideSoftInput()
        val foundFragment = supportFragmentManager.findFragmentByTag(tag)
        if (foundFragment is ScrollableScreen) {
            foundFragment.scrollUp()
        }
    }

    private fun showFragment(tag: String) {
        binding.root.hideSoftInput()
        val foundFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commitAllowingStateLoss()
        }
        foundFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: kotlin.run {
            val fragment = getFragmentByTag(tag)
            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment, tag)
                    .commitAllowingStateLoss()
            }
        }
    }

    private fun getFragmentByTag(tag: String): Fragment? =
        when (tag) {
            NewTabFragment.TAG -> NewTabFragment.newInstance()
            BookmarkTabFragment.TAG -> BookmarkTabFragment.newInstance()
            SearchTabFragment.TAG -> SearchTabFragment.newInstance()
            else -> null
        }

}
