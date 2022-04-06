package com.ihsan.kotlin.coroutine

import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.Executors

class ExecutorServiceTest {
    @Test
    fun testSingleThreadPool() {
        val executorService = Executors.newSingleThreadExecutor()
        repeat(10) {
            executorService.execute {
                Thread.sleep(1_000)
                println("Done $it ${Date()} in ${Thread.currentThread().name}")
            }
        }
        println("WAITING PROGRAM ${Date()}")
        Thread.sleep(11_000)
        println("DONE PROGRAM ${Date()}")
    }

    @Test
    fun testFixThreadPool() {
        val executorService = Executors.newFixedThreadPool(3)
        repeat(10) {
            executorService.execute {
                Thread.sleep(1_000)
                println("Done $it ${Date()} in ${Thread.currentThread().name}")
            }
        }
        println("WAITING PROGRAM ${Date()}")
        Thread.sleep(11_000)
        println("DONE PROGRAM ${Date()}")
    }

    @Test
    fun testCacheThreadPool() {
        val executorService = Executors.newCachedThreadPool()
        repeat(10) {
            executorService.execute {
                Thread.sleep(1_000)
                println("Done $it ${Date()} in ${Thread.currentThread().name}")
            }
        }
        println("WAITING PROGRAM ${Date()}")
        Thread.sleep(11_000)
        println("DONE PROGRAM ${Date()}")
    }
}