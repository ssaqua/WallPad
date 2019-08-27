package ss.wallpad.data.local

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import java.io.File
import java.lang.reflect.Type

class JsonFileCache<T>(
    context: Context,
    filename: String,
    private val type: Type,
    private val moshi: Moshi
) : Cache<T> {
    private val tag = JsonFileCache::class.java.name
    private val file = File(context.cacheDir, filename)

    override fun get(): T? {
        if (file.exists()) {
            try {
                val bufferedReader = file.bufferedReader()
                val jsonString = bufferedReader.use { it.readText() }
                val adapter = moshi.adapter<T?>(type)
                return adapter.fromJson(jsonString)
            } catch (e: Exception) {
                Log.i(tag, "Unable to read cached JSON file ${file.path} due to: ${e.message}")
            }
        }

        return null
    }

    override fun put(item: T) {
        try {
            val adapter = moshi.adapter<T>(type)
            val jsonString = adapter.toJson(item)
            file.writeText(jsonString)
        } catch (e: Exception) {
            Log.i(tag, "Unable to write JSON file at ${file.path} due to: ${e.message}")
        }
    }

    override fun timestamp(): Long? = if (file.exists()) file.lastModified() else null
}