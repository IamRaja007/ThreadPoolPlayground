package com.example.threadpoolplayground

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.*

class MainActivity : AppCompatActivity() {

    private lateinit var logView: TextView
    private lateinit var submitBtn: Button
    private lateinit var shutdownBtn: Button
    private lateinit var delayRangeSlider: SeekBar

    private var taskCount = 0
    private var minDelay = 1000
    private var maxDelay = 4000

    private lateinit var executor: ThreadPoolExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        executor = (application as MyApplication).threadPool

        logView = findViewById(R.id.logView)
        submitBtn = findViewById(R.id.submitTaskBtn)
        shutdownBtn = findViewById(R.id.shutdownBtn)
        delayRangeSlider = findViewById(R.id.delayRangeSlider)

        delayRangeSlider.max = 4000
        delayRangeSlider.progress = 2000
        delayRangeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                maxDelay = 1000 + progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                log("[INFO] Max delay set to ${maxDelay}ms", Color.BLUE)
            }
        })

        submitBtn.setOnClickListener {
            taskCount++
            val taskId = taskCount

            log("[SUBMIT] Task $taskId", Color.DKGRAY)

            val task = Runnable {
                val threadName = Thread.currentThread().name
                val delay = (minDelay..maxDelay).random()
                log("[RUNNING] Task $taskId on $threadName (delay: ${delay}ms)", Color.BLUE)
                Thread.sleep(delay.toLong())
                val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                log("[DONE $time] Task $taskId on $threadName", Color.parseColor("#388E3C"))
                logStatus()
            }

            try {
                executor.execute(task)
                logStatus()
            } catch (e: RejectedExecutionException) {
                // log("[REJECTED] Task $taskId: Queue full", Color.RED)
                if (executor.isShutdown) {
                    log("[REJECTED] Task $taskId: Executor is shut down", Color.RED)
                } else {
                    log("[REJECTED] Task $taskId: Queue is full", Color.RED)
                }
            } catch (e: Exception) {
                log("[ERROR] Task $taskId failed to submit: ${e.message}", Color.RED)
            }
        }

        shutdownBtn.setOnClickListener {
            log("[ACTION] Initiating shutdown...", Color.MAGENTA)
            executor.shutdown()
            log(
                "[STATUS] Is shutdown: ${executor.isShutdown} | Is terminated: ${executor.isTerminated}",
                Color.MAGENTA
            )
        }
    }

    private fun logStatus() {
        val active = executor.activeCount
        val poolSize = executor.poolSize
        val queueSize = executor.queue.size
        log("[STATUS] Pool: $poolSize | Active: $active | Queue: $queueSize", Color.GRAY)
    }

    private fun log(message: String, color: Int = Color.BLACK) {
        runOnUiThread {
            val spannable = SpannableStringBuilder(message + "\n")
            spannable.setSpan(ForegroundColorSpan(color), 0, spannable.length, 0)
            logView.append(spannable)
        }
    }
}
