package com.example.domain.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking


fun suspendedTest(body: suspend CoroutineScope.() -> Unit) {
    runBlocking { body() }
}