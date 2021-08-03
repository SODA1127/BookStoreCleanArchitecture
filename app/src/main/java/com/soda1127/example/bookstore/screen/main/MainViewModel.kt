package com.soda1127.example.bookstore.screen.main

import androidx.annotation.IdRes
import androidx.lifecycle.viewModelScope
import com.soda1127.example.bookstore.screen.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: BaseViewModel() {

    private val _navigationItemStateFlow = MutableStateFlow<MainNavigation?>(null)
    val navigationItemStateFlow: StateFlow<MainNavigation?> = _navigationItemStateFlow

    fun changeNavigation(mainNavigation: MainNavigation) = viewModelScope.launch {
        _navigationItemStateFlow.value = mainNavigation
    }

}

class MainNavigation(@IdRes val navigationMenuId: Int)
