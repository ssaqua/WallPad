package ss.wallpad.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ss.wallpad.data.SavedImageStore
import ss.wallpad.data.model.Image
import javax.inject.Inject

class SavedViewModel @Inject constructor(
    private val savedImageStore: SavedImageStore
) : ViewModel() {
    val savedImages: LiveData<List<Image>>
        get() {
            val data = MutableLiveData<List<Image>>()
            data.value = savedImageStore.get()
            return data
        }
}
