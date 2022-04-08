package com.ihsan.kotlin.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

class CoroutineDispatcherTest {

    @Test
    fun testDispatcher() {
        runBlocking {
            println("run Blocking ${Thread.currentThread().name}")
            val job1 = GlobalScope.launch(Dispatchers.Default) {
                println("Job 1 ${Thread.currentThread().name}")
            }
            val job2 = GlobalScope.launch(Dispatchers.IO) {
                println("Job 2 ${Thread.currentThread().name}")
            }
            joinAll(job1, job2)
        }
    }

    @Test
    fun testUnconfined() {
        runBlocking {
            println("run Blocking ${Thread.currentThread().name}")

            GlobalScope.launch(Dispatchers.Unconfined) {
                println("Unconfined : ${Thread.currentThread().name}")
                delay(1000)
                println("Unconfined : ${Thread.currentThread().name}")
            }
            GlobalScope.launch {
                println("Confined : ${Thread.currentThread().name}")
                delay(1000)
                println("Confined : ${Thread.currentThread().name}")
            }

            delay(2000)
        }
    }
}