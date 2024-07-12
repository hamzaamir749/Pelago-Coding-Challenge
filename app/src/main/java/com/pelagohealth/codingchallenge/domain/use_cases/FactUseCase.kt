package com.pelagohealth.codingchallenge.domain.use_cases

import com.pelagohealth.codingchallenge.domain.repository.FactRepository
import javax.inject.Inject

// Use case for fetching random facts
class FactUseCase @Inject constructor(private val factRepository: FactRepository) {
    suspend operator fun invoke() = factRepository.getFacts()
}