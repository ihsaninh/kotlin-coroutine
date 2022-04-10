package com.ihsan.kotlin.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.Test
import java.util.*

class FlowTest {

    @Test
    fun testFlow() {
        val flow: Flow<Int> = flow {
            println("Flow started")
            repeat(10) {
                println("emit $it")
                delay(1000)
                emit(it)
            }
        }

        runBlocking {
            flow.collect {
                println("Receive $it")
            }
        }
    }

    private fun numberFlow(): Flow<Int> = flow {
        repeat(100) {
            emit(it)
        }
    }

    private suspend fun changeToString(number: Int): String {
        delay(100)
        return "Number $number"
    }

    @Test
    fun testFlowOperator() {
        val flow = numberFlow()
        runBlocking {
            flow.filter { it % 2 == 0 }
                .map { changeToString(it) }
                .collect { println(it) }
        }
    }

    @Test
    fun testFlowException() {
        val flow = numberFlow()

        runBlocking {
            flow.map { check(it < 10); it }
                .onEach { println(it) }
                .catch { println("Error ${it.message}") }
                .onCompletion { println("Done") }
                .collect()
        }
    }

    @Test
    fun testFlowCancellable() {
        val flow = numberFlow()
        val scope = CoroutineScope(Dispatchers.IO)

        runBlocking {
            val job = scope.launch {
                flow.onEach {
                    if (it > 10) cancel()
                    else println("Number $it in ${Thread.currentThread().name}")
                }.collect()
            }

            job.join()
        }
    }

    @Test
    fun testSharedFlow() {
        val sharedFlow = MutableSharedFlow<Int>(10)
        val scope = CoroutineScope(Dispatchers.IO)

        runBlocking {
            scope.launch {
                repeat(10) {
                    println("send 1 : $it : ${Date()}")
                    delay(1000);
                    sharedFlow.emit(it)
                }
            }
            scope.launch {
                sharedFlow.asSharedFlow()
                    .buffer(10)
                    .map { "Receive job 1 : $it : ${Date()}" }
                    .collect {
                        delay(1000)
                        println(it)
                    }
            }
            scope.launch {
                sharedFlow.asSharedFlow()
                    .buffer(10)
                    .map { "Receive job 2 : $it : ${Date()}" }
                    .collect {
                        delay(2000)
                        println(it)
                    }
            }
            delay(22_000)
            scope.cancel()
        }
    }

    @Test
    fun testStateFlow() {
        val scope = CoroutineScope(Dispatchers.IO)
        val stateFlow = MutableStateFlow(0)

        runBlocking {
            scope.launch {
                repeat(10) {
                    println("send 1 : $it : ${Date()}")
                    delay(1000);
                    stateFlow.emit(it)
                }
            }
            scope.launch {
                stateFlow.asSharedFlow()
                    .map { "Receive job 2 : $it : ${Date()}" }
                    .collect {
                        delay(2000)
                        println(it)
                    }
            }
            delay(22_000)
            scope.cancel()
        }
    }
}