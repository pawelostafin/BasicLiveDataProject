package me.ostafin.livedatatutorial.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var SUT: MainViewModel
    private lateinit var bookRepositoryMock: BookRepository

    @Before
    fun setup() {
        bookRepositoryMock = mockk()
        SUT = MainViewModel(FAKE_ID_LONG, bookRepositoryMock)
    }

    @Test
    fun `string representation of ID emitted on showToast LiveData, after viewModel is initialized`() {
        // Arrange / given
        val mockedObserver = mockkedLiveDataObserver<String>()
        SUT.showToast.observeForever(mockedObserver)

        // Act / when
        SUT.initializeIfNeeded()

        // Assert / then
        val values = mutableListOf<String>()
        verify(exactly = 2) { mockedObserver.onChanged(capture(values)) }
        assertEquals(FAKE_ID_STRING + "elo", values[1])
    }

    @Test
    fun `repository function invoked when button clicked`() {
        // Arrange / given
        mockedRepoAnswer()
        SUT.initializeIfNeeded()

        // Act / when
        SUT.navigationButtonClicked()

        // Assert / then
        verify(exactly = 1) { bookRepositoryMock.giveMe667() }
    }

    private fun mockedRepoAnswer() {
        every { bookRepositoryMock.giveMe667() } answers { 668 }
    }

    companion object {
        private const val FAKE_ID_LONG = 111L
        private const val FAKE_ID_STRING = "111"
    }
}

inline fun <reified T : Any> mockkedLiveDataObserver(): Observer<T> {
    val mockedObserver = mockk<Observer<T>>()
    every { mockedObserver.onChanged(any()) } answers { Unit }
    return mockedObserver
}