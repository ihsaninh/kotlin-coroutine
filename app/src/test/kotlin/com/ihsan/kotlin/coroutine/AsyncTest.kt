package com.ihsan.kotlin.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class AsyncTest {
    private suspend fun getFoo(): Int {
        delay(1000)
        return 10
    }

    private suspend fun getBar(): Int {
        delay(1000)
        return 10
    }

    @Test
    fun testAsync() {
        runBlocking {
            val time: Long = measureTimeMillis {
                val foo: Deferred<Int> = GlobalScope.async { getFoo() }
                val bar: Deferred<Int> = GlobalScope.async { getBar() }

                val result: Int = foo.await() + bar.await()
                println("Result : $result")
            }
            println("Total Time $time")
        }
    }

    @Test
    fun testAwaitAll() {
        runBlocking {
            val time: Long = measureTimeMillis {
                val foo: Deferred<Int> = GlobalScope.async { getFoo() }
                val bar: Deferred<Int> = GlobalScope.async { getBar() }

                val result: Int = awaitAll(foo, bar).sum()
                println("Result : $result")
            }
            println("Total Time $time")
        }
    }
}