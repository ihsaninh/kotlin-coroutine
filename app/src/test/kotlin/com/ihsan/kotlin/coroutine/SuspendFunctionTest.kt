package com.ihsan.kotlin.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.*

class SuspendFunctionTest {
    @Test
    fun testSuspend() {
        runBlocking {
            helloWorld()
        }
    }

    private suspend fun helloWorld() {
        println("Hello World : ${Date()}")
        delay(2_000)
        println("Hello World 2 : ${Date()}")
    }
}