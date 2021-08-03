package com.soda1127.example.bookstore.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.soda1127.example.bookstore.di.testRepositoryModule
import com.soda1127.example.bookstore.di.testViewModelModule
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@InternalCoroutinesApi
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
internal abstract class ViewModelTest : KoinTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var context: Application

    private val dispatcher = TestCoroutineDispatcher()

    private val jobs = mutableListOf<Job>()

    @Before
    fun setup() {
        startKoin {
            androidContext(context)
            modules(testViewModelModule)
            modules(testRepositoryModule)
        }
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        stopKoin()
        jobs.filter { it.isActive }.forEach { it.cancel() }
        Dispatchers.resetMain() // MainDispatcher를 초기화 해주어야 메모리 누수가 발생하지 않음
    }

}
