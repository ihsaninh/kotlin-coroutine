package com.ihsan.kotlin.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

class SequentialSuspendFunction {
    private suspend fun getFoo(): Int {
        delay(1000)
        return 10
    }

    private suspend fun getBar(): Int {
        delay(1000)
        return 10
    }

    @Test
    fun testSequential() {
        runBlocking {
            val time = measureTimeMillis {
                getFoo()
                getBar()
            }
            println("Total time : $time")
        }
    }

    @Test
    fun testSequentialCoroutine() {
        runBlocking {
            val job = GlobalScope.launch {
                val time = measureTimeMillis {
                    getFoo()
                    getBar()
                }
                println("Total time : $time")
            }
            job.join()
        }
    }

    @Test
    fun testConcurrent() {
        runBlocking {
            val time = measureTimeMillis {
                val job1 = GlobalScope.launch { getFoo() }
                val job2 = GlobalScope.launch { getBar() }

                joinAll(job1, job2)
            }
            println("Total time : $time")
        }
    }

    private suspend fun runJob(number: Int) {
        println("Start job $number in Thread ${Thread.currentThread().name}")
        yield()
        println("End job $number in Thread ${Thread.currentThread().name}")
    }

    @Test
    fun testYieldFunction() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        runBlocking {
            scope.launch { runJob(1) }
            scope.launch { runJob(2) }

            delay(2000)
        }
    }
}