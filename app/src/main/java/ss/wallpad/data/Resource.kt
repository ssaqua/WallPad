package ss.wallpad.data

/**
 * Container for data with its loading status.
 */
data class Resource<out T>(val status: Status, val data: T? = null)

enum class Status {
    ERROR,
    LOADING,
    SUCCESS
}
