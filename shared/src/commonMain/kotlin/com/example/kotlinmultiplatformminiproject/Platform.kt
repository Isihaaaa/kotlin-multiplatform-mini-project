package com.example.kotlinmultiplatformminiproject

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform