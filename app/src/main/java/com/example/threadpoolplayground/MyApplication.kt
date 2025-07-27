package com.example.threadpoolplayground

import android.app.Application
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MyApplication : Application() {

    lateinit var threadPool: ThreadPoolExecutor
        private set

    override fun onCreate() {
        super.onCreate()

        val customThreadFactory = object : ThreadFactory {
            private var count = 0
            override fun newThread(r: Runnable): Thread {
                count++
                return Thread(r, "Raja's CustomThread-$count")
            }
        }

        threadPool = ThreadPoolExecutor(
            2, // core pool size
            4, // max pool size
            10, // keep-alive time
            TimeUnit.SECONDS,
            ArrayBlockingQueue(5),
            customThreadFactory,
            ThreadPoolExecutor.AbortPolicy()
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        threadPool.shutdown()
    }
}