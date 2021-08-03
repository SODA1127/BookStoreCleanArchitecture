package com.soda1127.example.bookstore.screen.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {

    open fun fetchData(): Job = viewModelScope.launch {  }

    protected val jobs = mutableListOf<Job>()

    override fun onCleared() {
        jobs.forEach { it.cancel() }
        super.onCleared()
    }

}
