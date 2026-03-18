package com.KotlinCrossPlatform.cotacao.data

import com.KotlinCrossPlatform.cotacao.domain.model.CurrencyQuote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CurrencyRemoteDataSource(private val client: HttpClient) {
    private val baseUrl = "https://economia.awesomeapi.com.br"

    suspend fun getLatestQuotes(pairs: String): Map<String, CurrencyQuote> {
        return client.get("$baseUrl/json/last/$pairs").body()
    }

    suspend fun getDailyHistory(pair: String, days: Int): List<CurrencyQuote> {
        return client.get("$baseUrl/json/daily/$pair/$days").body()
    }
}

fun createHttpClient() = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        })
    }
    install(Logging) {
        level = LogLevel.INFO
    }
}
