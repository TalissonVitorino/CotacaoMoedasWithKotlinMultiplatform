package com.KotlinCrossPlatform.cotacao.domain.repository

import com.KotlinCrossPlatform.cotacao.data.CurrencyRemoteDataSource
import com.KotlinCrossPlatform.cotacao.domain.model.CurrencyQuote

class CurrencyRepository(private val remoteDataSource: CurrencyRemoteDataSource) {
    suspend fun getHomeQuotes(): List<CurrencyQuote> {
        val pairs = "USD-BRL,EUR-BRL,GBP-BRL,ARS-BRL,CAD-BRL,AUD-BRL,JPY-BRL,CHF-BRL,CNY-BRL,BTC-BRL,ETH-BRL,LTC-BRL,XRP-BRL,DOGE-BRL,CLP-BRL,PYG-BRL,UYU-BRL,COP-BRL,PEN-BRL,MXN-BRL"
        val response = remoteDataSource.getLatestQuotes(pairs)
        return response.values.toList()
    }

    suspend fun getHistory(pair: String): List<CurrencyQuote> {
        return remoteDataSource.getDailyHistory(pair, 10)
    }
}
