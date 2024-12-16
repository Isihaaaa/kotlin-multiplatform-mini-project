package com.example.kotlinmultiplatformminiproject.android.ui.eventmodify

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.kotlinmultiplatformminiproject.Event
import com.example.kotlinmultiplatformminiproject.android.ui.manager.EventManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventModifyViewModel(
    private val eventId: Int,
    private val eventManager: EventManager
) : ViewModel(), DefaultLifecycleObserver {
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
        val event = eventManager.getEventById(eventId)
        return BusinessData(
           event = event
        )
    }

    //// EVENT HANDLING

    fun modifiedEvent(event: Event?, navController: NavController) {
        if (event == null) return

        viewModelScope.launch {
            try {
                _showLoading.value = true
                delay(1000)

                eventManager.modifyEvent(event)

                navController.popBackStack()
            } catch (e: Exception) {
                Log.e("EventCreateViewModel", "Error: ${e.message}")
            } finally {
                _showLoading.value = false
            }
        }
    }

    fun onNameChanged(text: String) {
        businessData.update {
            it!!.copy(
                event = it.event.copy(name = text),
            )
        }
    }


    fun onCityChanged(text: String) {
        businessData.update {
            it!!.copy(
                event = it.event.copy(location = text),
            )
        }
    }

    fun onCountryChanged(text: String) {
        businessData.update {
            it!!.copy(
                event = it.event.copy(country = text),
            )
        }
    }

    fun onCapacityChanged(text: String) {
        businessData.update {
            it!!.copy(
                event = it.event.copy(capacity = text.toInt()),
            )
        }
    }


        //// OBJECT HOLDERS

    data class BusinessData(
        val event: Event,
    ) {
        fun toUIState() = UIState(
            event = event,
        )
    }

    data class UIState(
        val event: Event,
    )
}