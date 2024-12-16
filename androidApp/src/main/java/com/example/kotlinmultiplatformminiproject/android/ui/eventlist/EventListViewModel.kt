package com.example.kotlinmultiplatformminiproject.android.ui.eventlist

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmultiplatformminiproject.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventListViewModel : ViewModel(), DefaultLifecycleObserver {
    val uiState: StateFlow<UIState?>
    private val businessData: MutableStateFlow<BusinessData?> = MutableStateFlow(null)

    protected val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    //// STATE HANDLING

    init {
        uiState = businessData.map {
            try {
                it?.toUIState()
            } catch (e: Exception) {
                Log.e("EventModifyViewModel", "Error: ${e.message}")

                null // do not update the UI state
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

        viewModelScope.launch {
            initBusinessData()
        }
    }

    private fun initBusinessData() {
        try {
            businessData.value = createBusinessData()
        } catch (e: Exception) {
            Log.e("EventModifyViewModel", "Error: ${e.message}")
        }
    }

    private fun createBusinessData(): BusinessData {

        return BusinessData(
            events = listOf(
                Event(
                    id = 0,
                    name = "",
                    location = "",
                    country = "",
                    capacity = 0
                )
            )
        )
    }

    //// EVENT HANDLING

    //// OBJECT HOLDERS

    data class BusinessData(
        val events: List<Event>,
    ) {
        fun toUIState() =
            UIState(
                events = events,
            )
    }

    data class UIState(
        val events: List<Event>,
    )
}