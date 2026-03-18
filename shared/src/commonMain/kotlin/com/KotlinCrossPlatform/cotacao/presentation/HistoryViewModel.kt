package com.KotlinCrossPlatform.cotacao.presentation

import com.KotlinCrossPlatform.cotacao.domain.model.CurrencyQuote
import com.KotlinCrossPlatform.cotacao.domain.repository.CurrencyRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HistoryUiState {
    object Loading : HistoryUiState()
    data class Success(val history: List<CurrencyQuote>) : HistoryUiState()
    data class Error(val message: String) : HistoryUiState()
}

class HistoryViewModel(private val repository: CurrencyRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    fun loadHistory(pair: String) {
        viewModelScope.launch {
            _uiState.value = HistoryUiState.Loading
            try {
                val history = repository.getHistory(pair)
                _uiState.value = HistoryUiState.Success(history)
            } catch (e: Exception) {
                _uiState.value = HistoryUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
