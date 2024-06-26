package com.healthy.sehatscan.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthy.sehatscan.data.repository.DrinkHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val drinkHistoryRepository: DrinkHistoryRepository
) : ViewModel() {
    val drinkHistoryList = drinkHistoryRepository.drinkHistoryList
    val isHistoryLoading = drinkHistoryRepository.isHistoryLoading
    val historyErrorMessage = drinkHistoryRepository.historyErrorMessage

    fun getDrinkHistory() {
        viewModelScope.launch {
            drinkHistoryRepository.getDrinkHistory()
        }
    }
}