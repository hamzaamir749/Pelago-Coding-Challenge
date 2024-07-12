package com.pelagohealth.codingchallenge.data.mappers

import com.pelagohealth.codingchallenge.data.datasource.model.FactDTO
import com.pelagohealth.codingchallenge.domain.model.Fact


//Convert DTO to Domain
fun FactDTO.toDomain(): Fact {
    return Fact(text = this.text, url = this.sourceUrl)
}