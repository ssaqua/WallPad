package ss.wallpad.data

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import ss.wallpad.data.model.Image
import java.io.File

class JsonFileSavedImageStore(
    context: Context,
    savedDirectory: String,
    moshi: Moshi
) : SavedImageStore {
    private val tag = "JsonImageStore"
    private val directory = File(context.filesDir, savedDirectory)
    private val adapter = moshi.adapter(Image::class.java)

    override fun get(): List<Image> {
        val list: MutableList<Image> = mutableListOf()
        if (directory.exists()) {
            directory.listFiles { file -> file.extension == "json" }.forEach { file ->
                try {
                    val bufferedReader = file.bufferedReader()
                    val jsonString = bufferedReader.use { it.readText() }
                    list.add(adapter.fromJson(jsonString)!!)
                } catch (e: Exception) {
                    Log.i(tag, "Unable to read saved JSON file ${file.path} due to: ${e.message}.")
                }
            }
        }
        return list
    }

    override fun put(image: Image) {
        if (!directory.exists()) {
            directory.mkdir()
        }
        val file = File(directory, image.imageId + ".json")
        try {
            val jsonString = adapter.toJson(image)
            file.writeText(jsonString)
        } catch (e: Exception) {
            Log.i(tag, "Unable to write JSON file at ${file.path} due to: ${e.message}")
        }
    }

    override fun delete(image: Image) {
        val file = File(directory, image.imageId + ".json")
        if (file.exists()) file.delete()
    }
}
