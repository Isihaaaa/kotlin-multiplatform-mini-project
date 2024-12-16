package com.example.kotlinmultiplatformminiproject
/**
 * Represents a Event.
 *
 * @property name required
 * @property location required, maximum length 100
 * @property country  not required
 * @property capacity not required, positive integer
 */
data class Event(
    val id: Int,
    val name: String,
    val location: String,
    val country: String,
    val capacity: Int
)
