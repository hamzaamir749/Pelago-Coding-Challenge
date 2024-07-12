package com.pelagohealth.codingchallenge.data.repository

import com.pelagohealth.codingchallenge.data.datasource.rest.FactsRestApi
import com.pelagohealth.codingchallenge.data.mappers.toDomain
import com.pelagohealth.codingchallenge.domain.model.Fact
import com.pelagohealth.codingchallenge.domain.repository.FactRepository
import com.pelagohealth.codingchallenge.domain.utils.Response
import com.squareup.moshi.JsonDataException
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Repository providing random facts.
 */

class FactRepositoryImpl @Inject constructor(private val factsRestApi: FactsRestApi) :
    FactRepository {
        /**
     * Returns a random fact.
     */

    override suspend fun getFacts(): Response<Fact, String> {
        return try {
            val fact = factsRestApi.getFact().toDomain()
          //Send the fact to the use cases
            Response.Success(fact)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string() ?: "Unknown error"
            Response.Error("HTTP Error: ${e.code()} $errorBody")
        } catch (io: IOException) {
            Response.Error(io.message.toString())
        } catch (json: JsonDataException) {
            Response.Error("JSON Error: ${json.message}")
        }
    }

}