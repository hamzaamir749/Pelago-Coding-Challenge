package com.pelagohealth.codingchallenge.presentation.utils

import com.pelagohealth.codingchallenge.domain.model.Fact

data class MainStates(
    val isLoading: Boolean = false,
    val fact: List<Fact>? = null,
    val error: String? = null
)

