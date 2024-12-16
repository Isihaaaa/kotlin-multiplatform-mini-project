package com.example.kotlinmultiplatformminiproject.android.ui.eventlist

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.kotlinmultiplatformminiproject.Event
import com.example.kotlinmultiplatformminiproject.android.ui.manager.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventListViewModel(
    private val eventManager: EventManager
) : ViewModel(), DefaultLifecycleObserver {
    val uiState: StateFlow<UIState?>
    private val businessData: MutableStateFlow<BusinessData?> = MutableStateFlow(null)

    protected val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    //// STATE HANDLING

    init {
        uiState = combine(
            businessData,
            eventManager.eventParameters
        ) { businessData, eventManager ->
            try {
                businessData?.toUIState(eventManager)
            } catch (e: Exception) {
                Log.e("EventModifyViewModel", "Error: ${e.message}")

                null // do not update the UI state
            }
        }.map { it }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

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
            events = eventManager.eventParameters.value.events
        )
    }

    //// EVENT HANDLING

    fun deleteEvent(event: Event) {
        businessData.value?.events?.let {
            val newEvents = it.toMutableList()
            newEvents.remove(event)
            businessData.value = businessData.value?.copy(events = newEvents)
        }

        eventManager.deleteEvent(event)
    }

    fun navigateToModifyEvent(event: Event, navController: NavController) {
        // navigate to modify event screen
        navController.navigate("Route.EVENT_MODIFY/${event.id}")
    }

    //// OBJECT HOLDERS

    data class BusinessData(
        val events: List<Event>,
    ) {
        fun toUIState(eventParameters: EventManager.EventParameters) =
            UIState(
                events = eventParameters.events,
            )
    }

    data class UIState(
        val events: List<Event>,
    )
}