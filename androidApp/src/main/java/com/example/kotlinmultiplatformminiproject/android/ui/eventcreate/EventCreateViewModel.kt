package com.example.kotlinmultiplatformminiproject.android.ui.eventcreate

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.Database
import com.example.kotlinmultiplatformminiproject.domain.Event
import com.example.kotlinmultiplatformminiproject.android.ui.manager.EventManager
import com.example.kotlinmultiplatformminiproject.database.insertOrReplaceEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class EventCreateViewModel(
    private val eventManager: EventManager,
    private val db: Database
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

        return BusinessData(
            event = Event(
                id = 0,
                name = "",
                location = "",
                country = "",
                capacity = 0
            )
        )
    }

    //// EVENT HANDLING

    fun addNewEvent(event: Event, navController: NavController) {
        viewModelScope.launch {
            try {
                _showLoading.value = true
                delay(1000)

                insertOrReplaceEvent(db, Event(Random.nextInt(), event.name, event.location, event.country, event.capacity))

//                eventManager.addNewEvent(
//                    Event(
//                        id = eventManager.eventParameters.value.events.size + 1,
//                        name = event.name,
//                        location = event.location,
//                        country = event.country,
//                        capacity = event.capacity
//                    )
//                )

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
        fun toUIState() =
            UIState(
                event = event,
            )
    }

    data class UIState(
        val event: Event,
    )
}