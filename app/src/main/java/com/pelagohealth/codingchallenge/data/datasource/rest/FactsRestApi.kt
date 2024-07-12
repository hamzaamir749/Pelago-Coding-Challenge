package com.pelagohealth.codingchallenge.data.datasource.rest

import com.pelagohealth.codingchallenge.data.datasource.model.FactDTO
import retrofit2.http.GET

/**
 * REST API for fetching random facts.
 */
interface FactsRestApi {

    @GET("facts/random")
    suspend fun getFact(): FactDTO
}