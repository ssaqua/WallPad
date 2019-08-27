package ss.wallpad.data

import ss.wallpad.data.model.Image

interface SavedImageStore {
    fun get(): List<Image>
    fun put(image: Image)
    fun delete(image: Image)
}
