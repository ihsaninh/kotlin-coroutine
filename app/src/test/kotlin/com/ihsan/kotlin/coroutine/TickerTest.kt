package com.ihsan.kotlin.coroutine

import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.*

class TickerTest {

    @Test
    fun tickerTest() {
        val receiveChannel = ticker(delayMillis = 1000)

        runBlocking {
            val job = launch {
                repeat(10) {
                    receiveChannel.receive()
                    println(Date())
                }
            }
            job.join()
        }
    }
}