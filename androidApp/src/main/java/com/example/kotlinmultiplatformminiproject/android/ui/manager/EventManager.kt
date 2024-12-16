package com.example.kotlinmultiplatformminiproject.android.ui.manager

import com.example.kotlinmultiplatformminiproject.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class EventManager {
    val eventParameters = MutableStateFlow(EventParameters.default())

    fun addNewEvent(event: Event) {
        eventParameters.update {
            it.copy(events = it.events + event)
        }
    }

    fun getEventById(id: Int): Event {
        return eventParameters.value.events.first { it.id == id }
    }

    fun modifyEvent(event: Event) {
        eventParameters.update {
            it.copy(events = it.events.map { e ->
                if (e.id == event.id) {
                    event
                } else {
                    e
                }
            })
        }
    }

    fun deleteEvent(event: Event) {
        eventParameters.update {
            it.copy(events = it.events.filter { e -> e.id != event.id })
        }
    }

    data class EventParameters(
        val events: List<Event>,
    ) {
        companion object {
            fun default(): EventParameters {
                return EventParameters(
                    events = listOf(
                        Event(
                            id = 0,
                            name = "Event 1",
                            location = "location 1",
                            country = "country 1",
                            capacity = 0
                        )
                    )
                )
            }
        }
    }
}