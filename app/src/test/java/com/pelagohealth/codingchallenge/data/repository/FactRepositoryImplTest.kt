package com.pelagohealth.codingchallenge.data.repository

import com.pelagohealth.codingchallenge.data.datasource.model.FactDTO
import com.pelagohealth.codingchallenge.data.datasource.rest.FactsRestApi
import com.pelagohealth.codingchallenge.data.mappers.toDomain
import com.pelagohealth.codingchallenge.domain.utils.Response
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.HttpException
import java.io.IOException


class FactRepositoryImplTest {

    private val factsRestApi: FactsRestApi = mock()

    @Test
    fun test_fact_success() = runTest {
        `when`(factsRestApi.getFact()).thenReturn(getFakeFact())
        val repository = FactRepositoryImpl(factsRestApi)
        val result = repository.getFacts()
        assertEquals(getFakeFact().toDomain(), (result as Response.Success).data)

    }

    @Test
    fun test_fact_io_error() = runTest {
        `when`(factsRestApi.getFact()).thenAnswer { throw IOException("IO Error") }
        val repository = FactRepositoryImpl(factsRestApi)
        val result = repository.getFacts()
        assertEquals("IO Error", (result as Response.Error).error)
    }

    @Test
    fun test_fact_json_error() = runTest {
        `when`(factsRestApi.getFact()).thenAnswer { throw JsonDataException("JSON Error") }
        val repository = FactRepositoryImpl(factsRestApi)
        val result = repository.getFacts()
        assertEquals("JSON Error: JSON Error", (result as Response.Error).error)
    }

    @Test
    fun test_fact_http_error() = runTest {
        val errorResponse = retrofit2.Response.error<Any>(
            500,
            "HTTP".toResponseBody("application/json".toMediaTypeOrNull())
        )
        `when`(factsRestApi.getFact()).thenAnswer { throw HttpException(errorResponse) }
        val repository = FactRepositoryImpl(factsRestApi)
        val result = repository.getFacts()
        assertEquals("HTTP Error: 500 HTTP", (result as Response.Error).error)
    }


}

private fun getFakeFact(): FactDTO {
    return FactDTO(
        id = "3b23527ba33a9c2f2100e63d5237dd69",
        text = "Barbie`s measurements, if she were life-size, would be 39-29-33.",
        sourceUrl = "http://www.djtech.net/humor/useless_facts.htm"
    )
}