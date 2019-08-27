package ss.wallpad.data

interface TimeProvider {
    fun currentTimeMillis(): Long
}
