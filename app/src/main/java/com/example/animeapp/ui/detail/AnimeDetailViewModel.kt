package com.example.animeapp.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.data.JikanRepository
import com.example.animeapp.data.model.AnimeDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val repository: JikanRepository
) : ViewModel() {

    private val _detail = MutableStateFlow<AnimeDetail?>(null)
    val detail: StateFlow<AnimeDetail?> = _detail

    private val _uiState = MutableStateFlow<AnimeDetailUiState>(AnimeDetailUiState.Loading)
    val uiState: StateFlow<AnimeDetailUiState> = _uiState

    fun load(id: Int) {
        viewModelScope.launch {
            _uiState.value = AnimeDetailUiState.Loading
            try {
                val detail = repository.getAnimeDetail(id)
                if (detail != null) {
                    _uiState.value = AnimeDetailUiState.Success(detail)
                } else {
                    _uiState.value = AnimeDetailUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = AnimeDetailUiState.Error
            }
        }
    }
}
