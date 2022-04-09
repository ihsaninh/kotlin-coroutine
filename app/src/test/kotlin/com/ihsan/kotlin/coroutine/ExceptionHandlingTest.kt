package com.ihsan.kotlin.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

class ExceptionHandlingTest {

    @Test
    fun testExceptionLaunch() {
        runBlocking {
            val job = GlobalScope.launch {
                println("Start coroutine")
                throw IllegalArgumentException()
            }

            job.join()
            println("Finish")
        }
    }

    @Test
    fun testExceptionAsync() {
        runBlocking {
            val deferred = GlobalScope.async<String> {
                println("launch coroutine")
                throw IllegalArgumentException()
            }
            try {
                deferred.await()
                println("Finish async")
            } catch (error: IllegalArgumentException) {
                println("Error")
            } finally {
                println("Finally async")
            }
        }
    }

    @Test
    fun testExceptionHandler() {
        val exceptionHandler = CoroutineExceptionHandler {
                _, throwable ->
            println("Ups, error with message ${throwable.message}")
        }
        val scope = CoroutineScope(exceptionHandler + Dispatchers.IO)
        val job = scope.launch(exceptionHandler) {
            println("job run")
            throw IllegalArgumentException("Ups")
        }

        runBlocking {
            job.join()
            println("Finish")
        }
    }
}