package ss.wallpad.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ss.wallpad.ViewModelFactory
import ss.wallpad.ui.collection.CollectionViewModel
import ss.wallpad.ui.gallery.GalleryViewModel
import ss.wallpad.ui.saved.SavedViewModel

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CollectionViewModel::class)
    abstract fun bindCollectionViewModel(collectionViewModel: CollectionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    abstract fun bindGalleryViewModel(galleryViewModel: GalleryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavedViewModel::class)
    abstract fun bindSavedViewModel(savedViewModel: SavedViewModel): ViewModel
}
