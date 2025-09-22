package com.example.animeapp.ui.detail

import com.example.animeapp.data.model.AnimeDetail

sealed class AnimeDetailUiState {
    object Loading : AnimeDetailUiState()
    data class Success(val detail: AnimeDetail) : AnimeDetailUiState()
    object Error : AnimeDetailUiState()
}
