package ss.wallpad.ui.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ss.wallpad.data.Resource
import ss.wallpad.data.model.Collection
import ss.wallpad.data.repository.CollectionRepository
import ss.wallpad.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class CollectionViewModel @Inject constructor(
    collectionRepository: CollectionRepository
) : ViewModel() {
    val collections: LiveData<Resource<List<Collection>>> = collectionRepository.getCollections()
}
