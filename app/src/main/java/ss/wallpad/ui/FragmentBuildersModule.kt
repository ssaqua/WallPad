package ss.wallpad.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ss.wallpad.ui.collection.CollectionFragment
import ss.wallpad.ui.gallery.GalleryFragment
import ss.wallpad.ui.photoviewer.PhotoViewerFragment
import ss.wallpad.ui.saved.SavedFragment

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeCollectionFragment(): CollectionFragment

    @ContributesAndroidInjector
    abstract fun contributeGalleryFragment(): GalleryFragment

    @ContributesAndroidInjector
    abstract fun contributeSavedFragment(): SavedFragment

    @ContributesAndroidInjector
    abstract fun contributePhotoViewerFragment(): PhotoViewerFragment
}
