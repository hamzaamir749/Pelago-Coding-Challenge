package com.pelagohealth.codingchallenge.data.datasource.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//Model for receiving data from the API
@JsonClass(generateAdapter = true)
data class FactDTO(
    val id: String,
    val text: String,
    @Json(name = "source_url")
    val sourceUrl: String
)