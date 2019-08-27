package ss.wallpad.ui.collection

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import ss.wallpad.R
import ss.wallpad.TestApplication
import ss.wallpad.data.Resource
import ss.wallpad.data.Status
import ss.wallpad.data.model.Collection
import ss.wallpad.espresso.matcher.atPosition
import ss.wallpad.espresso.matcher.hasItemCount
import ss.wallpad.mock

@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class CollectionFragmentTest {
    val mockCollectionViewModel: CollectionViewModel = mock()
    val mockCollections = MutableLiveData<Resource<List<Collection>>>()

    val testCollections = listOf(
        Collection("mock_1", "http://example.com/mock_image_1.jpg"),
        Collection("mock_2", "http://example.com/mock_image_2.jpg"),
        Collection("mock_3", "http://example.com/mock_image_3.jpg"),
        Collection("mock_4", "http://example.com/mock_image_4.jpg"),
        Collection("mock_5", "http://example.com/mock_image_5.jpg"),
        Collection("mock_6", "http://example.com/mock_image_6.jpg"),
        Collection("mock_7", "http://example.com/mock_image_7.jpg"),
        Collection("mock_8", "http://example.com/mock_image_8.jpg")
    )

    lateinit var scenario: FragmentScenario<CollectionFragment>

    class TestCollectionFragment : CollectionFragment() {
        val navController: NavController = mock()
        override fun navController() = navController
    }

    @Before
    fun setUp() {
        `when`(mockCollectionViewModel.collections).thenReturn(mockCollections)
        scenario = launchFragmentInContainer {
            val mockViewModelFactory: ViewModelProvider.Factory = mock()
            `when`(mockViewModelFactory.create(CollectionViewModel::class.java))
                .thenReturn(mockCollectionViewModel)
            TestCollectionFragment().apply { viewModelFactory = mockViewModelFactory }
        }
    }

    @Test
    fun progressBarVisible_onLoadingStatus() {
        mockCollections.postValue(Resource(Status.LOADING))

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.empty_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.collection_recycler_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun emptyTextVisible_onSuccessNull() {
        mockCollections.postValue(Resource(Status.SUCCESS, null))

        onView(withId(R.id.empty_text)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.collection_recycler_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun emptyTextVisible_onSuccessEmptyList() {
        mockCollections.postValue(Resource(Status.SUCCESS, emptyList()))

        onView(withId(R.id.empty_text)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.collection_recycler_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun errorTextVisible_onErrorStatus() {
        mockCollections.postValue(Resource(Status.ERROR))

        onView(withId(R.id.error_text)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.empty_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.collection_recycler_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun collectionRecyclerViewVisible_onSuccessCollections() {
        mockCollections.postValue(Resource(Status.SUCCESS, testCollections))

        onView(withId(R.id.collection_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.empty_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_text)).check(matches(not(isDisplayed())))

        onView(withId(R.id.collection_recycler_view)).check(matches(hasItemCount(8)))
        testCollections.forEachIndexed { index, collection ->
            onView(withId(R.id.collection_recycler_view))
                .perform(scrollToPosition<RecyclerView.ViewHolder>(index))
                .check(matches(atPosition(
                    index,
                    hasDescendant(allOf(withId(R.id.text_view), withText(collection.name)))
                )))
        }
    }

    @Test
    fun clickCollectionItems_navigatesToGallery() {
        mockCollections.postValue(Resource(Status.SUCCESS, testCollections))

        testCollections.forEachIndexed { index, collection ->
            onView(withId(R.id.collection_recycler_view))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(index, click()))
            scenario.onFragment { fragment ->
                verify(fragment.navController())
                    .navigate(CollectionFragmentDirections.openGallery(collection.name))
            }
        }
    }
}
