package com.cabbage.fireticv2.data

/**
 * The outcome holds the state of the screen. It can be progress, success or failure
 * https://medium.com/mindorks/effective-networking-on-android-using-retrofit-rx-and-architecture-components-4554ca5b167d
 */
sealed class Outcome<T> {
    /**
     * where isLoading represents data lookup & can be used to show or hide a progress bar.
     */
    data class Progress<T>(var loading: Boolean) : Outcome<T>()

    /**
     * where data is the data you would want to show on the UI
     */
    data class Success<T>(var data: T) : Outcome<T>()

    /**
     * where exception represents any error that could have failed the data lookup.
     */
    data class Failure<T>(val e: Throwable) : Outcome<T>()

    companion object {
        fun <T> progress(isLoading: Boolean): Outcome<T> = Progress(isLoading)

        fun <T> success(data: T): Outcome<T> = Success(data)

        fun <T> failure(e: Throwable): Outcome<T> = Failure(e)
    }
}