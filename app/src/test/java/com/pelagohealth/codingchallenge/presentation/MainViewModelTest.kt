package com.pelagohealth.codingchallenge.presentation

import com.pelagohealth.codingchallenge.domain.model.Fact
import com.pelagohealth.codingchallenge.domain.use_cases.FactUseCase
import com.pelagohealth.codingchallenge.domain.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val useCase: FactUseCase = mock()
    private val viewModel = MainViewModel(useCase)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchNewFact() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        val fact = Fact("Test Fact", "https://example.com")

        `when`(useCase.invoke()).thenReturn(Response.Success(fact))
        viewModel.fetchNewFact()

        assertEquals(true, viewModel.state.isLoading)
        assertNull(viewModel.state.error)

        advanceUntilIdle()

        assertEquals(false, viewModel.state.isLoading)
        assertNull(viewModel.state.error)
        assertEquals(listOf(fact), viewModel.state.fact)
        Dispatchers.resetMain()

    }

    @Test
    fun deleteItem() {
        val fact1 = Fact("Test Fact", "https://example.com")
        val fact2 = Fact("Test Fact", "https://example.com")
        viewModel.limitedList.add(fact1)
        viewModel.limitedList.add(fact2)
        viewModel.state = viewModel.state.copy(fact = viewModel.limitedList.getItems())

        viewModel.deleteItem(0)

        assertEquals(listOf(fact2), viewModel.state.fact)
    }


    class MainDispatcherRule @OptIn(ExperimentalCoroutinesApi::class) constructor(val dispatcher: TestDispatcher = UnconfinedTestDispatcher()) :
        TestWatcher() {

        override fun starting(description: Description?) = Dispatchers.setMain(dispatcher)

        override fun finished(description: Description?) = Dispatchers.resetMain()

    }
}


