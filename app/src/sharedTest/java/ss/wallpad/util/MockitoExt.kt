package ss.wallpad.util

import org.mockito.ArgumentMatchers
import org.mockito.Mockito

/**
 * Kotlin friendly mock to handle generics.
 */
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

/**
 * Kotlin friendly any argument matcher to handle generics.
 */
inline fun <reified T> any(): T = ArgumentMatchers.any(T::class.java)
