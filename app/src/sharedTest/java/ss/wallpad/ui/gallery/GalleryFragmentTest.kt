package ss.wallpad.ui.gallery

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import ss.wallpad.R
import ss.wallpad.TestApplication
import ss.wallpad.any
import ss.wallpad.data.Resource
import ss.wallpad.data.Status
import ss.wallpad.data.model.Image
import ss.wallpad.espresso.matcher.atPosition
import ss.wallpad.espresso.matcher.hasItemCount
import ss.wallpad.espresso.matcher.hasTransitionName
import ss.wallpad.mock

@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class GalleryFragmentTest {
    val mockGalleryViewModel: GalleryViewModel = mock()

    val imagesLiveData = MutableLiveData<Resource<List<Image>>>()
    val testImages = listOf(
        Image("mock_1", "mock_1", "http://example.com/mock_image_1_thumb.jpg", "http://example.com/mock_image_1.jpg"),
        Image("mock_2", "mock_2", "http://example.com/mock_image_2_thumb.jpg", "http://example.com/mock_image_2.jpg"),
        Image("mock_3", "mock_3", "http://example.com/mock_image_3_thumb.jpg", "http://example.com/mock_image_3.jpg"),
        Image("mock_4", "mock_4", "http://example.com/mock_image_4_thumb.jpg", "http://example.com/mock_image_4.jpg"),
        Image("mock_5", "mock_5", "http://example.com/mock_image_5_thumb.jpg", "http://example.com/mock_image_5.jpg"),
        Image("mock_6", "mock_6", "http://example.com/mock_image_6_thumb.jpg", "http://example.com/mock_image_6.jpg"),
        Image("mock_7", "mock_7", "http://example.com/mock_image_7_thumb.jpg", "http://example.com/mock_image_7.jpg"),
        Image("mock_8", "mock_8", "http://example.com/mock_image_8_thumb.jpg", "http://example.com/mock_image_8.jpg"),
        Image("mock_9", "mock_9", "http://example.com/mock_image_9_thumb.jpg", "http://example.com/mock_image_9.jpg")
    )

    class TestGalleryFragment : GalleryFragment() {
        val navController: NavController = mock()
        override fun navController() = navController
    }

    lateinit var scenario: FragmentScenario<GalleryFragment>

    @Before
    fun setUp() {
        `when`(mockGalleryViewModel.images).thenReturn(imagesLiveData)
        scenario = launchFragmentInContainer(
            fragmentArgs = Bundle().apply { putString("query", "test_query") },
            themeResId = R.style.AppTheme
        ) {
            val mockViewModelFactory: ViewModelProvider.Factory = mock()
            `when`(mockViewModelFactory.create(GalleryViewModel::class.java))
                .thenReturn(mockGalleryViewModel)
            TestGalleryFragment().apply { viewModelFactory = mockViewModelFactory }
        }
    }

    @Test
    fun progressBarVisible_onLoadingStatus() {
        imagesLiveData.postValue(Resource(Status.LOADING))

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.empty_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.gallery_recycler_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun emptyTextVisible_onSuccessNull() {
        imagesLiveData.postValue(Resource(Status.SUCCESS, null))

        onView(withId(R.id.empty_text)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.gallery_recycler_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun emptyTextVisible_onSuccessEmptyList() {
        imagesLiveData.postValue(Resource(Status.SUCCESS, emptyList()))

        onView(withId(R.id.empty_text)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.gallery_recycler_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun errorTextVisible_onErrorStatus() {
        imagesLiveData.postValue(Resource(Status.ERROR))

        onView(withId(R.id.error_text)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.empty_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.gallery_recycler_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun galleryRecyclerViewVisible_onSuccessImages() {
        imagesLiveData.postValue(Resource(Status.SUCCESS, testImages))

        onView(withId(R.id.gallery_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.empty_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_text)).check(matches(not(isDisplayed())))

        onView(withId(R.id.gallery_recycler_view)).check(matches(hasItemCount(testImages.size)))
        testImages.forEachIndexed { index, image ->
            onView(withId(R.id.gallery_recycler_view))
                .perform(scrollToPosition<RecyclerView.ViewHolder>(index))
                .check(matches(atPosition(
                    index,
                    hasDescendant(allOf(withId(R.id.image_view), hasTransitionName(image.imageId)))
                )))
        }
    }

    @Test
    fun clickImageItems_navigatesToPhotoViewer() {
        imagesLiveData.postValue(Resource(Status.SUCCESS, testImages))

        testImages.forEachIndexed { index, image ->
            onView(withId(R.id.gallery_recycler_view))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(index, click()))
            scenario.onFragment { fragment ->
                verify(fragment.navController())
                    .navigate(eq(GalleryFragmentDirections.photoViewer(image)), any<Navigator.Extras>())
            }
        }
    }
}
