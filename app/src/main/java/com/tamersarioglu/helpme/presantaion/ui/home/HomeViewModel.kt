package com.tamersarioglu.helpme.presantaion.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.helpme.domain.model.Earthquake
import com.tamersarioglu.helpme.domain.usecase.GetEarthquakesUseCase
import com.tamersarioglu.helpme.presantaion.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

// Modify HomeViewModel.kt to add sorting capability

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getEarthquakesUseCase: GetEarthquakesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<EarthquakesState>(EarthquakesState())
    val state: StateFlow<EarthquakesState> = _state

    private var currentSortType = SortType.TIME
    private var earthquakes = emptyList<Earthquake>()

    init {
        getEarthquakes()
    }

    fun getEarthquakes() {
        getEarthquakesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    earthquakes = result.data ?: emptyList()
                    applySorting(currentSortType)
                }
                is Resource.Error -> {
                    _state.value = EarthquakesState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _state.value = EarthquakesState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setSortType(sortType: SortType) {
        currentSortType = sortType
        applySorting(sortType)
    }

    private fun applySorting(sortType: SortType) {
        val sortedList = when (sortType) {
            SortType.MAGNITUDE_DESC -> earthquakes.sortedByDescending { it.magnitude }
            SortType.MAGNITUDE_ASC -> earthquakes.sortedBy { it.magnitude }
            SortType.DEPTH_DESC -> earthquakes.sortedByDescending { it.depth }
            SortType.DEPTH_ASC -> earthquakes.sortedBy { it.depth }
            SortType.TIME -> {
                earthquakes.sortedByDescending {
                    "${it.date} ${it.time}"
                }
            }
        }

        _state.value = EarthquakesState(earthquakes = sortedList)
    }

    enum class SortType {
        MAGNITUDE_DESC, MAGNITUDE_ASC, DEPTH_DESC, DEPTH_ASC, TIME
    }

    data class EarthquakesState(
        val isLoading: Boolean = false,
        val earthquakes: List<Earthquake> = emptyList(),
        val error: String = ""
    )
}