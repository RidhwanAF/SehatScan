package com.healthy.sehatscan.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthy.sehatscan.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: FavoriteRepository
) : ViewModel() {

    val favoriteDrink = repository.favoriteList
    val isFavoriteLoading = repository.isFavoriteLoading
    val favoriteErrorMessage = repository.favoriteErrorMessage

    fun getFavoriteDrink() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserFavorite()
        }
    }
}