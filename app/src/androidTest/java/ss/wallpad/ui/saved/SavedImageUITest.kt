package ss.wallpad.ui.saved

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import retrofit2.mock.Calls
import ss.wallpad.data.model.Collection
import ss.wallpad.data.model.Image
import ss.wallpad.data.model.Images
import ss.wallpad.ui.DaggerTest
import ss.wallpad.ui.MainActivity
import ss.wallpad.ui.collection.collection
import ss.wallpad.ui.gallery.gallery
import ss.wallpad.ui.photoviewer.photoViewer

@RunWith(AndroidJUnit4::class)
class SavedImageUITest : DaggerTest() {
    val mockCollectionService = testAppComponent.mockCollectionService()
    val testCollections = listOf(
        Collection("mock_1", "http://example.com/mock_image_1.jpg")
    )
    val mockBingImageSearchService = testAppComponent.mockBingImageSearchService()
    val savedImageStore = testAppComponent.savedImageStore()
    val testImages = Images(listOf(
        Image("mock_1", "mock_1", "http://example.com/mock_image_1_thumb.jpg", "http://example.com/mock_image_1.jpg"),
        Image("mock_2", "mock_2", "http://example.com/mock_image_2_thumb.jpg", "http://example.com/mock_image_2.jpg")
    ))

    lateinit var scenario: ActivityScenario<MainActivity>

    @After
    fun tearDown() {
        scenario.close()
    }

    /**
     * User interaction flow:
     *   - given empty saved image collection
     *   - navigate to a specific image's photoviewer via collection > gallery > photoviewer
     *   - save the image
     *   - navigate back to saved image collection
     *   - assert the image has been saved
     */
    @Test
    fun saveImageUserFlow() {
        `when`(mockCollectionService.getCollections()).thenReturn(Calls.response(testCollections))
        `when`(mockBingImageSearchService.search("mock_1")).thenReturn(Calls.response(testImages))
        scenario = launchActivity()

        collection {

        } saved {
            isEmpty()
        } collection {
            enterCollection(testCollections.first())
        }
        gallery {
            enterImageAtIndex(0)
        }
        photoViewer {
            save()
        } saved {
            hasSavedImages(testImages.value.first())
        }
    }

    /**
     * User interaction flow:
     *   - given a non-empty saved image collection
     *   - navigate to a saved image's photoviewer
     *   - delete the saved image
     *   - assert the image has been removed from saved images
     *   - repeat until saved image collection is empty
     */
    @Test
    fun deleteSavedImageUserFlow() {
        `when`(mockCollectionService.getCollections()).thenReturn(Calls.response(testCollections))
        testImages.value.forEach { savedImageStore.put(it) }
        scenario = launchActivity()

        collection {

        } saved {
            hasSavedImages(testImages.value[0], testImages.value[1])
            enterImageAtIndex(0)
        }
        photoViewer {

        } delete {
            hasSavedImages(testImages.value[1])
            enterImageAtIndex(0)
        }
        photoViewer {

        } delete {
            isEmpty()
        }
    }
}
