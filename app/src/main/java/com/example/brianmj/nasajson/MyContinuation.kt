package com.example.brianmj.nasajson

import android.os.Handler
import android.os.Looper
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.ContinuationInterceptor

private class MyContinuation<T>(val cont: Continuation<T>) : Continuation<T> by cont {
    override fun resume(value: T) {
        if(Looper.getMainLooper() == Looper.myLooper()) cont.resume(value)      // we're on the main thread
        else Handler(Looper.getMainLooper()).post {cont.resume(value)}          // attach a handler to the looper of the main thread
        // and post this continuation on it.
    }

    override fun resumeWithException(exception: Throwable) {
        if(Looper.getMainLooper() == Looper.myLooper()) { cont.resumeWithException(exception)}
        else Handler(Looper.getMainLooper()).post {cont.resumeWithException(exception)}
    }
}

// AndroidUI - the coroutine context [fit???] for running a continuation on the main thread
object AndroidUI : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        return MyContinuation(continuation)
    }
}