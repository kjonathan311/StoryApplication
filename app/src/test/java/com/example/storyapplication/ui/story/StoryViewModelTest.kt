package com.example.storyapplication.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapplication.DataDummy
import com.example.storyapplication.MainDispatcherRule
import com.example.storyapplication.data.remote.response.ListStoryItem
import com.example.storyapplication.data.repositories.StoryRepository
import com.example.storyapplication.data.repositories.UserRepository
import com.example.storyapplication.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun `When Get List Stories Should not Return Not Null But Return Data`()=runTest{
        val dummyStory=DataDummy.generateDummyStoryListResponse()
        val data:PagingData<ListStoryItem> =StoryPagingSource.snapshot(dummyStory)
        val expectedStoryList=MutableLiveData<PagingData<ListStoryItem>>()
        expectedStoryList.value=data
        Mockito.`when`(storyRepository.getListStories()).thenReturn(expectedStoryList)

        val storyViewModel=StoryViewModel(storyRepository=storyRepository, userRepository = userRepository)
        val actualStoryList:PagingData<ListStoryItem> =storyViewModel.listStory.getOrAwaitValue()

        val differ=AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStoryList)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get List Stories Is Empty And Should Return With No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStoryList = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStoryList.value = data
        Mockito.`when`(storyRepository.getListStories()).thenReturn(expectedStoryList)
        val storyViewModel=StoryViewModel(storyRepository=storyRepository, userRepository = userRepository)
        val actualStoryList: PagingData<ListStoryItem> = storyViewModel.listStory.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStoryList)
        assertEquals(0, differ.snapshot().size)

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }
}

}