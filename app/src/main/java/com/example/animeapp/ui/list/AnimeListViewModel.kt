package com.example.animeapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.data.JikanRepository
import com.example.animeapp.data.model.TopResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeListViewModel @Inject constructor(
    private val repository: JikanRepository
) : ViewModel() {

    private val _anime = MutableStateFlow<TopResult?>(null)
    val anime: StateFlow<TopResult?> = _anime.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAnime()
    }

    fun loadAnime(onLoaded: () -> Unit = {}) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            repository.refreshTopAnime()
            onLoaded.invoke()
            repository.topAnimeFlow().collectLatest { result ->
                _anime.value = result
                _loading.value = false
            }
        }
    }
}
