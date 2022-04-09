package com.ihsan.kotlin.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class CoroutineContextTest {
    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun testCoroutineContext() {
        runBlocking {
            val job = GlobalScope.launch {
                val context: CoroutineContext = coroutineContext
                println(context)
                println(context[Job])
                println(context[CoroutineDispatcher])
            }
            job.join()
        }
    }

    @Test
    fun testCoroutineName() {
        val scope = CoroutineScope(Dispatchers.IO)

        val job = scope.launch(CoroutineName("parent")) {
            println("Parent run in thread ${Thread.currentThread().name}")
            withContext(CoroutineName("child")) {
                println("Parent run in thread ${Thread.currentThread().name}")
            }
        }

        runBlocking {
            job.join()
        }
    }

    @Test
    fun testCoroutineElements() {
        val dispatcher = Executors.newFixedThreadPool((10)).asCoroutineDispatcher()
        val scope = CoroutineScope(Dispatchers.IO + CoroutineName("test"))

        val job = scope.launch(CoroutineName("parent") + dispatcher) {
            println("Parent run in thread ${Thread.currentThread().name}")
            withContext(CoroutineName("child") + Dispatchers.IO) {
                println("Parent run in thread ${Thread.currentThread().name}")
            }
        }

        runBlocking {
            job.join()
        }
    }
}