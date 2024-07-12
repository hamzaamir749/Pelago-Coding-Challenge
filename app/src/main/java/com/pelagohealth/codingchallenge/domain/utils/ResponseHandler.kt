package com.pelagohealth.codingchallenge.domain.utils



// Handle response api calls
sealed interface Response<out D, out E> {
    data class Success<out D, out E>(val data: D) : Response<D, E>
    data class Error<out D, out E>(val error: E) : Response<D, E>
}

