package com.pelagohealth.codingchallenge.domain.repository

import com.pelagohealth.codingchallenge.domain.model.Fact
import com.pelagohealth.codingchallenge.domain.utils.Response


/**
 * Repository providing random facts.
 */

interface FactRepository {
    /**
     * Get a random fact.
     */
    suspend fun getFacts(): Response<Fact, String>
}