package com.example.kotlinmultiplatformminiproject

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return platform.name
    }
}