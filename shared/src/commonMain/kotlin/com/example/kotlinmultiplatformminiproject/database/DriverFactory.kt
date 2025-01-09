package com.example.kotlinmultiplatformminiproject.database

import app.cash.sqldelight.db.SqlDriver
import com.example.Database
import com.example.kotlinmultiplatformminiproject.domain.Event

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): Database {
    val driver = driverFactory.createDriver()
    val database = Database(driver)

    // Do more work with the database (see below).
    return database
}

fun getAllEvents(database: Database): List<Event> {
    return database.eventQueries.getAllEvents().executeAsList().map {
        Event(it.id.toInt(), it.name, it.location, it.country, it.capacity.toInt())
    }
}

fun getEventById(id: Long, database: Database): Event? {
    val event = database.eventQueries.getEventById(id).executeAsOneOrNull() ?: return null

    return Event(
        event.id.toInt(),
        event.name,
        event.location,
        event.country,
        event.capacity.toInt()
    )
}

fun insertOrReplaceEvent(database: Database, event: Event) {
    database.eventQueries.insertOrReplaceEvent(
        event.id.toLong(),
        event.name,
        event.location,
        event.country,
        event.capacity.toLong()
    )
}

fun deleteEventById(id: Long, database: Database) {
    database.eventQueries.deleteEventById(id)
}