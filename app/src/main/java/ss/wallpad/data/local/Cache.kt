package ss.wallpad.data.local

interface Cache<T> {
    fun get(): T?
    fun put(item: T)
    fun timestamp(): Long?
}
