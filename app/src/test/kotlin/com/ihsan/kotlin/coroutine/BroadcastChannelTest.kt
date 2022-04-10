package com.ihsan.kotlin.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.broadcast
import org.junit.jupiter.api.Test

class BroadcastChannelTest {

    @Test
    fun testBroadcastChannel() {
        val broadcastChannel = BroadcastChannel<Int>(capacity = 10)
        val receiver1 = broadcastChannel.openSubscription()
        val receiver2 = broadcastChannel.openSubscription()
        val scope = CoroutineScope(Dispatchers.IO)

        runBlocking {
            val jobSend = scope.launch {
                repeat(10) { broadcastChannel.send(it) }
            }
            val job1 = scope.launch {
                repeat(10) { println("Receiver 1 : ${receiver1.receive()}") }
            }
            val job2 = scope.launch {
                repeat(10) { println("Receiver 2 : ${receiver2.receive()}") }
            }

            joinAll(job1, job2, jobSend)
        }
    }

    @Test
    fun testBroadcastFunction() {
        val scope = CoroutineScope(Dispatchers.IO)
        val broadcastChannel = scope.broadcast<Int>(capacity = 10) {
            repeat(10) { send(it) }
        }
        val receiver1 = broadcastChannel.openSubscription()
        val receiver2 = broadcastChannel.openSubscription()

        runBlocking {
            val job1 = scope.launch {
                repeat(10) { println("Receiver 1 : ${receiver1.receive()}") }
            }
            val job2 = scope.launch {
                repeat(10) { println("Receiver 2 : ${receiver2.receive()}") }
            }

            joinAll(job1, job2)
        }
    }

    @Test
    fun testConflatedBroadcastChannel() {
        val conflatedBroadcastChannel = ConflatedBroadcastChannel<Int>()
        val receiver = conflatedBroadcastChannel.openSubscription()
        val scope = CoroutineScope(Dispatchers.IO)

        runBlocking {
            val jobSend = scope.launch {
                repeat(10) {
                    delay(1000)
                    println("Send $it")
                    conflatedBroadcastChannel.send(it)
                }
            }
            val job1 = scope.launch {
                repeat(10) {
                    delay(2000)
                    println("Receiver : ${receiver.receive()}")
                }
            }

            delay(11000)
            scope.cancel()
            joinAll(job1, jobSend)
        }
    }
}