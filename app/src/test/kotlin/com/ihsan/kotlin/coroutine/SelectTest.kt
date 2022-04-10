package com.ihsan.kotlin.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import org.junit.jupiter.api.Test

class SelectTest {

    @Test
    fun testSelectDeferred() {
        val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        val deferred1 = scope.async { delay(1000); 1000 }
        val deferred2 = scope.async { delay(2000); 2000 }

        runBlocking {
            val win = select<Int> {
                deferred1.onAwait { it }
                deferred2.onAwait { it }
            }

            println("Win : $win")
        }
    }

    @Test
    fun testSelectChannel() {
        val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        val channel1: ReceiveChannel<Int> = scope.produce { delay(1000); send(1000) }
        val channel2: ReceiveChannel<Int> = scope.produce { delay(2000); send(2000) }

        runBlocking {
            val win = select<Int> {
                channel1.onReceive { it }
                channel2.onReceive { it }
            }

            println("Win : $win")
        }
    }

    @Test
    fun testSelectDeferredChannel() {
        val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        val channel1: ReceiveChannel<Int> = scope.produce { delay(1000); send(1000) }
        val channel2: ReceiveChannel<Int> = scope.produce { delay(2000); send(2000) }
        val deferred1: Deferred<Int> = scope.async { delay(100); 500 }
        val deferred2: Deferred<Int> = scope.async { delay(1000); 600 }

        runBlocking {
            val win = select<Int> {
                channel1.onReceive { it }
                channel2.onReceive { it }
                deferred1.onAwait { it }
                deferred2.onAwait { it }
            }

            println("Win : $win")
        }
    }
}