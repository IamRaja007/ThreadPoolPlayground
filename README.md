# Android ThreadPoolExecutor Playground

🚀 A simple, visual and interactive Android app to experiment with `ThreadPoolExecutor` and understand how thread pools behave in real-world scenarios.

## Features

* Submit background tasks to a `ThreadPoolExecutor`
* Control task execution delay using a SeekBar
* View real-time logs for task submission, execution, and completion
* Monitor pool status: active threads, queue size, and pool size
* Gracefully shut down the executor at runtime
* Handles task rejection due to shutdown or full queue
* Uses a custom `ThreadFactory` for named threads

## Purpose

This app was built as part of a learning exercise to explore low-level thread management on Android. While coroutines and higher-level libraries like WorkManager are common today, understanding the basics of executors is still essential for performance tuning, custom scheduling, and building reliable background systems.

## How to Use

1. Clone the repository

2. Open the project in Android Studio.

3. Run the app on a device or emulator (API 24+).

4. Tap the "Submit Task" button to start background tasks.

5. Use the slider to adjust task delay dynamically.

6. Tap "Shutdown" to gracefully terminate the executor.

## Tech Stack

* Kotlin
* Android
* ThreadPoolExecutor
* Basic UI components (TextView, Button, SeekBar)


