package ss.wallpad.ui.gallery

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import ss.wallpad.R
import ss.wallpad.TestApplication
import ss.wallpad.data.Resource
import ss.wallpad.data.Status
import ss.wallpad.data.model.Image
import ss.wallpad.mock

@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class GalleryFragmentTest {
    val mockGalleryViewModel: GalleryViewModel = mock()

    val imagesLiveData = MutableLiveData<Resource<List<Image>>>()

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
}
