package ss.wallpad.data.remote

import dagger.Module
import dagger.Provides
import ss.wallpad.util.mock
import javax.inject.Singleton

/**
 * Mock [ApiModule] that replaces retrofit services with Mockito mocks.
 * The retrofit-mock artifact has useful factory methods for creating Call instances,
 * see [retrofit2.mock.Calls].
 */
@Module
class MockApiModule {
    @Provides
    @Singleton
    fun provideBingImageSearchService(): BingImageSearchService = mock()

    @Provides
    @Singleton
    fun provideCollectionService(): CollectionService = mock()
}
