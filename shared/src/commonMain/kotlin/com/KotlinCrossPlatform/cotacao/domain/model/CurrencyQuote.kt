package com.KotlinCrossPlatform.cotacao.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyQuote(
    val code: String,
    @SerialName("codein")
    val codeIn: String,
    val name: String,
    val high: String? = null,
    val low: String? = null,
    val varBid: String? = null,
    val pctChange: String? = null,
    val bid: String,
    val ask: String? = null,
    val timestamp: String? = null,
    @SerialName("create_date")
    val createDate: String? = null
)
