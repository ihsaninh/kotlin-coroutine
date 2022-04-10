package com.ihsan.kotlin.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class SupervisorJobTest {

    @Test
    fun testSupervisorJob() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher + SupervisorJob())

        val job1 = scope.launch {
            delay(2000)
            println("Job 1 done")
        }

        val job2 = scope.launch {
            delay(2000)
            throw IllegalArgumentException("Job 2 failed")
        }

        runBlocking {
            joinAll(job1, job2)
        }
    }

    @Test
    fun testSupervisorScopeFunction() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher + Job())

        runBlocking {
            scope.launch {
                supervisorScope {
                    launch {
                        delay(2000)
                        println("Child 1 done")
                    }
                    launch {
                        delay(2000)
                        throw IllegalArgumentException("Child 2 error")
                    }
                }
            }

            delay(3000)
        }
    }

    @Test
    fun testExceptionSupervisorJob() {
        val exceptionHandler = CoroutineExceptionHandler {
                _: CoroutineContext, throwable: Throwable ->
            println("Error happen ${throwable.message}")
        }
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(Job() + dispatcher)

        runBlocking {
            val job = scope.launch {
                supervisorScope {
                    launch(exceptionHandler) {
                        launch {
                            println("Job 1")
                            throw IllegalArgumentException("Job 1 error")
                        }
                    }
                }
            }
            job.join()
        }
    }
}