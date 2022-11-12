/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.picloud

import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TestChannelFlow {

    suspend fun emitNumber() = channelFlow {
        send("Started")
        var number = 1
        while (number<=10) {
            kotlinx.coroutines.delay(1000)
            number++
            send("New Number $number")
        }
    }

    suspend fun emitAlpha() = channelFlow {
        send("Started")
        var number = 'a'
        while (number<='z') {
            kotlinx.coroutines.delay(1000)
            number++
            send("New alpha $number")
        }
    }

    suspend fun sync() = channelFlow {
        send("Sync Started")
        emitNumber().collect {
            send(it)
        }
        emitAlpha().collect {
            send(it)
        }
    }

    @Test
    fun testSync() {
        runBlocking {
            sync().onEach {
                println(it)
            }.collect()
        }
    }
}