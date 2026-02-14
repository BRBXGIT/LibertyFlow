package com.example.common.dispatchers

import javax.inject.Qualifier

/**
 * Qualifier annotation used to distinguish between different types of
 * [LibertyFlowDispatcher] instances.
 * * This is typically used in dependency injection frameworks (like Dagger or Hilt)
 * to identify which coroutine dispatcher should be provided to a component.
 * @property libertyFlowDispatcher The specific dispatcher type associated with this qualifier.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val libertyFlowDispatcher: LibertyFlowDispatcher)

/**
 * Represents the available threading strategies for LibertyFlow operations.
 */
enum class LibertyFlowDispatcher {
    /**
     * Optimized for CPU-intensive work (e.g., sorting large lists or complex calculations).
     */
    Default,

    /**
     * Optimized for I/O-bound tasks (e.g., network requests, database operations, or file reading).
     */
    IO,

    /**
     * Confines execution to the main UI thread.
     * Used for UI updates or interactions with framework-specific main loops.
     */
    Main
}