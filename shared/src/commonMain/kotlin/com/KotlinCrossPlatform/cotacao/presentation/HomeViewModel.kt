package com.KotlinCrossPlatform.cotacao.presentation

import com.KotlinCrossPlatform.cotacao.domain.model.CurrencyQuote
import com.KotlinCrossPlatform.cotacao.domain.repository.CurrencyRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val quotes: List<CurrencyQuote>,
        val btcQuote: CurrencyQuote? = null,
        val highlightedQuotes: List<CurrencyQuote> = emptyList(),
        val otherQuotes: List<CurrencyQuote> = emptyList()
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(private val repository: CurrencyRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _brlAmount = MutableStateFlow("1.00")
    val brlAmount: StateFlow<String> = _brlAmount.asStateFlow()

    init {
        loadQuotes()
    }

    fun loadQuotes() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val quotes = repository.getHomeQuotes()
                val highlighted = quotes.filter { it.code == "USD" || it.code == "EUR" }
                val others = quotes.filter { it.code != "USD" && it.code != "EUR" }.sortedBy { it.name }
                _uiState.value = HomeUiState.Success(
                    quotes = quotes,
                    highlightedQuotes = highlighted,
                    otherQuotes = others
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun onAmountChange(newAmount: String) {
        if (newAmount.isEmpty() || newAmount.toDoubleOrNull() != null || newAmount.endsWith(".")) {
            _brlAmount.value = newAmount
        }
    }
}
