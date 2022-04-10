package com.ihsan.kotlin.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import org.junit.jupiter.api.Test

class ChannelTest {
    @Test
    fun testChannel() {
        runBlocking {
            val channel = Channel<Int>()
            val job2 = launch {
                println("Send 1")
                channel.send(1)
                println("Send 2")
                channel.send(2)
            }
            val job1 = launch {
                println("Receive ${channel.receive()}")
                println("Receive ${channel.receive()}")
            }
            joinAll(job1, job2)
            channel.close()
        }
    }

    @Test
    fun testChannelUnlimited() {
        runBlocking {
            val channel = Channel<Int>(capacity = Channel.UNLIMITED)
            val job2 = launch {
                println("Send 1")
                channel.send(1)
                println("Send 2")
                channel.send(2)
            }
            val job1 = launch {
                println("Receive ${channel.receive()}")
                println("Receive ${channel.receive()}")
            }
            joinAll(job1, job2)
            channel.close()
        }
    }

    @Test
    fun testChannelConflated() {
        runBlocking {
            val channel = Channel<Int>(capacity = Channel.UNLIMITED)
            val job1 = launch {
                println("Send 1")
                channel.send(1)
                println("Send 2")
                channel.send(2)
            }
            job1.join()
            val job2 = launch {
                println("Receive ${channel.receive()}")
            }
            job2.join()
            channel.close()
        }
    }

    @Test
    fun testChannelBufferOverflow() {
        runBlocking {
            val channel = Channel<Int>(capacity = 5, onBufferOverflow = BufferOverflow.DROP_OLDEST)
            val job1 = launch {
                repeat(10) {
                    channel.send(it)
                }
            }
            joinAll(job1)
            delay(1000)
            val job2 = launch {
                repeat(5) {
                    println("Receive ${channel.receive()}")
                }
            }
            job2.join()
            channel.close()
        }
    }

    @Test
    fun testChannelUndelivered() {
        runBlocking {
            val channel = Channel<Int>(capacity = Channel.UNLIMITED) {
                println("Undelivered value $it")
            }
            channel.close()
            val job = launch {
                channel.send(10)
            }
            joinAll(job)
        }
    }

    @Test
    fun testProduce() {
        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            val channel: ReceiveChannel<Int> = scope.produce {
                repeat(100) {
                    send(it)
                }
            }

            val job = scope.launch {
                repeat(100) {
                    println(channel.receive())
                }
            }

            runBlocking {
                joinAll(job)
            }
        }
    }
}