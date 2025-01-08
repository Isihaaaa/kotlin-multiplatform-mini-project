package com.example.kotlinmultiplatformminiproject

import com.example.kotlinmultiplatformminiproject.database.EventDataBase

interface Platform {
    val name: String
}

//interface IEventDataBase {
//    val db: EventDataBase
//}

expect fun getPlatform(): Platform
abstract fun getEventDataBase(): EventDataBase

class Shared {
    private val platform: Platform = getPlatform()
    private val dataBase: EventDataBase = getEventDataBase()

    fun greet(): String {
        return platform.name
    }

    fun getEventDao(): EventDataBase {
        return dataBase
    }
}