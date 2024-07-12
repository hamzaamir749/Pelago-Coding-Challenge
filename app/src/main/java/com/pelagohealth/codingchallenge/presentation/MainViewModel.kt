package com.pelagohealth.codingchallenge.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pelagohealth.codingchallenge.domain.model.Fact
import com.pelagohealth.codingchallenge.domain.use_cases.FactUseCase
import com.pelagohealth.codingchallenge.domain.utils.Response
import com.pelagohealth.codingchallenge.presentation.utils.MainStates
import com.pelagohealth.codingchallenge.presentation.utils.LimitedList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val useCase: FactUseCase): ViewModel() {
    val limitedList = LimitedList<Fact>(3)
    //handle state of the screen
    var state by mutableStateOf(MainStates())

    init {
        fetchNewFact()
    }

    fun fetchNewFact() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            when (val result = useCase.invoke()) {
                is Response.Success -> {
                    limitedList.add(result.data)
                    state = state.copy(
                        fact = limitedList.getItems(),
                        isLoading = false,
                        error = null
                    )

                }
                is Response.Error -> {
                    state = state.copy(
                        fact = limitedList.getItems(),
                        isLoading = false,
                        error = result.error
                    )
                }

            }
        }

    }

    fun deleteItem(position: Int) {
        limitedList.remove(position)
        state = state.copy(
            fact = limitedList.getItems(),
            isLoading = false,
            error = null
        )
    }

}