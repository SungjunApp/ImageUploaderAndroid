package com.android.app.ui.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.android.app.rule.CoroutinesTestRule
import com.android.app.util.PixleeTestUtil
import com.pixlee.pixleesdk.PXLAlbumSortType
import com.sjsoft.app.constant.AppConfig
import com.sjsoft.app.data.PXLPhotoItem
import com.sjsoft.app.data.repository.PixleeDataSource
import com.sjsoft.app.data.repository.PreferenceDataSource
import com.sjsoft.app.ui.gallery.GalleryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GalleryViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var pixlee: PixleeDataSource

    @Mock
    lateinit var pref: PreferenceDataSource

    private lateinit var viewModel: GalleryViewModel

    @Mock
    private lateinit var listObserver: Observer<GalleryViewModel.ListUI>

    @Captor
    private lateinit var listCaptor: ArgumentCaptor<GalleryViewModel.ListUI>

    @Mock
    private lateinit var loadMoreObserver: Observer<Boolean>

    @Captor
    private lateinit var loadMoreCaptor: ArgumentCaptor<Boolean>

    @Mock
    private lateinit var sortTypeObserver: Observer<PXLAlbumSortType>

    @Captor
    private lateinit var sortTypeCaptor: ArgumentCaptor<PXLAlbumSortType>

    @ExperimentalCoroutinesApi
    @Before
    fun setupViewModel() {
        MockitoAnnotations.initMocks(this)
        viewModel = GalleryViewModel(pixlee ,pref)
        viewModel.listUI.observeForever(listObserver)
        viewModel.loadMoreUI.observeForever(loadMoreObserver)
        viewModel.sortType.observeForever(sortTypeObserver)
    }

    @After
    fun after() {
        viewModel.listUI.removeObserver(listObserver)
        viewModel.loadMoreUI.removeObserver(loadMoreObserver)
        viewModel.sortType.removeObserver(sortTypeObserver)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `load a list succeeded`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val firstItems = PixleeTestUtil.getPhotoItems()
        val secondItems = PixleeTestUtil.getPhotoItems(1)

        val type = PXLAlbumSortType.RECENCY
        val options = viewModel.generateSortOption(type)

        //First call
        `when`(pixlee.loadNextPageOfPhotos(options)).thenReturn(
            flow {
                emit(firstItems)
            }
        )

        //Second call
        `when`(pixlee.loadNextPageOfPhotos()).thenReturn(
            flow {
                emit(secondItems)
            }
        )

        //Select Tab
        viewModel.changeTab(options)

        //Scroll down to get the next page
        viewModel.listScrolled(
            visibleItemCount = 18,
            lastVisibleItemPosition = 20,
            totalItemCount = 40
        )

        verify(sortTypeObserver, times(1)).onChanged(sortTypeCaptor.capture())
        Assert.assertEquals(
            type,
            sortTypeCaptor.allValues[0]
        )

        verify(listObserver, times(3)).onChanged(listCaptor.capture())
        Assert.assertEquals(
            GalleryViewModel.ListUI.LoadingShown,
            listCaptor.allValues[0]
        )

        //Since PXLPhoto class is not testable class, this code only verify idx
        Assert.assertEquals(
            GalleryViewModel.ListUI.Data(firstItems).list[0].idx,
            (listCaptor.allValues[1] as GalleryViewModel.ListUI.Data).list[0].idx
        )

        Assert.assertEquals(
            GalleryViewModel.ListUI.Data(secondItems).list[0].idx,
            (listCaptor.allValues[2] as GalleryViewModel.ListUI.Data).list[0].idx
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `change tabs succeeded`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val firstItems = PixleeTestUtil.getPhotoItems()
        val secondItems = PixleeTestUtil.getPhotoItems(1)

        val firstType = PXLAlbumSortType.RECENCY
        val firstOptions = viewModel.generateSortOption(firstType)

        val secondType = PXLAlbumSortType.DYNAMIC
        val secondOptions = viewModel.generateSortOption(secondType)

        //a call for firstType
        `when`(pixlee.loadNextPageOfPhotos(firstOptions)).thenReturn(
            flow {
                emit(firstItems)
            }
        )

        //another call for secondType
        `when`(pixlee.loadNextPageOfPhotos(secondOptions)).thenReturn(
            flow {
                emit(secondItems)
            }
        )

        //Select the first tab
        viewModel.changeTab(firstOptions)

        //advanceTimeBy(AppConfig.splashDelay)

        //Select the second tab
        viewModel.changeTab(secondOptions)

        verify(sortTypeObserver, times(2)).onChanged(sortTypeCaptor.capture())
        Assert.assertEquals(
            firstType,
            sortTypeCaptor.allValues[0]
        )

        Assert.assertEquals(
            secondType,
            sortTypeCaptor.allValues[1]
        )

        verify(listObserver, times(4)).onChanged(listCaptor.capture())
        Assert.assertEquals(
            GalleryViewModel.ListUI.LoadingShown,
            listCaptor.allValues[0]
        )

        //Since PXLPhoto class is not testable class, this code only verify idx
        Assert.assertEquals(
            GalleryViewModel.ListUI.Data(firstItems).list[0].idx,
            (listCaptor.allValues[1] as GalleryViewModel.ListUI.Data).list[0].idx
        )

        Assert.assertEquals(
            GalleryViewModel.ListUI.LoadingShown,
            listCaptor.allValues[2]
        )

        Assert.assertEquals(
            GalleryViewModel.ListUI.Data(secondItems).list[0].idx,
            (listCaptor.allValues[3] as GalleryViewModel.ListUI.Data).list[0].idx
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `load the first list failed`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val firstItems = PixleeTestUtil.getPhotoItems()

        val type = PXLAlbumSortType.RECENCY
        val options = viewModel.generateSortOption(type)

        `when`(pixlee.loadNextPageOfPhotos(options)).thenThrow(IllegalArgumentException(""))

        viewModel.changeTab(options)

        verify(sortTypeObserver, times(1)).onChanged(sortTypeCaptor.capture())
        Assert.assertEquals(
            type,
            sortTypeCaptor.allValues[0]
        )

        verify(listObserver, times(2)).onChanged(listCaptor.capture())
        Assert.assertEquals(
            GalleryViewModel.ListUI.LoadingShown,
            listCaptor.allValues[0]
        )

        //Since PXLPhoto class is not testable class, this code only verify idx
        Assert.assertEquals(
            GalleryViewModel.ListUI.LoadingHide,
            listCaptor.allValues[1]
        )

        verify(loadMoreObserver, times(3)).onChanged(loadMoreCaptor.capture())
        Assert.assertEquals(false, loadMoreCaptor.allValues[0])
        Assert.assertEquals(false, loadMoreCaptor.allValues[1])
        Assert.assertEquals(true, loadMoreCaptor.allValues[2])
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `load the second list failed`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val firstItems = PixleeTestUtil.getPhotoItems()

        val type = PXLAlbumSortType.RECENCY
        val options = viewModel.generateSortOption(type)

        `when`(pixlee.loadNextPageOfPhotos(options)).thenReturn(
            flow {
                emit(firstItems)
            }
        )

        `when`(pixlee.loadNextPageOfPhotos()).thenThrow(IllegalArgumentException(""))

        viewModel.changeTab(options)

        viewModel.listScrolled(
            visibleItemCount = 18,
            lastVisibleItemPosition = 20,
            totalItemCount = 40
        )

        verify(sortTypeObserver, times(1)).onChanged(sortTypeCaptor.capture())
        Assert.assertEquals(
            type,
            sortTypeCaptor.allValues[0]
        )

        verify(listObserver, times(2)).onChanged(listCaptor.capture())
        Assert.assertEquals(
            GalleryViewModel.ListUI.LoadingShown,
            listCaptor.allValues[0]
        )

        //Since PXLPhoto class is not testable class, this code only verify idx
        Assert.assertEquals(
            GalleryViewModel.ListUI.Data(firstItems).list[0].idx,
            (listCaptor.allValues[1] as GalleryViewModel.ListUI.Data).list[0].idx
        )

        verify(loadMoreObserver, times(4)).onChanged(loadMoreCaptor.capture())
        Assert.assertEquals(false, loadMoreCaptor.allValues[0])
        Assert.assertEquals(false, loadMoreCaptor.allValues[1])
        Assert.assertEquals(false, loadMoreCaptor.allValues[2])
        Assert.assertEquals(true, loadMoreCaptor.allValues[3])
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `load the third list failed`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val firstItems = PixleeTestUtil.getPhotoItems()
        val secondItems = PixleeTestUtil.getPhotoItems(1)

        val type = PXLAlbumSortType.RECENCY
        val options = viewModel.generateSortOption(type)

        `when`(pixlee.loadNextPageOfPhotos(options)).thenReturn(
            flow {
                emit(firstItems)
            }
        )
        viewModel.changeTab(options)

        `when`(pixlee.loadNextPageOfPhotos()).thenReturn(
            flow {
                emit(secondItems)
            }
        )
        viewModel.listScrolled(
            visibleItemCount = 18,
            lastVisibleItemPosition = 20,
            totalItemCount = 40
        )

        `when`(pixlee.loadNextPageOfPhotos()).thenThrow(IllegalArgumentException(""))
        viewModel.listScrolled(
            visibleItemCount = 18,
            lastVisibleItemPosition = 59,
            totalItemCount = 80
        )

        verify(sortTypeObserver, times(1)).onChanged(sortTypeCaptor.capture())
        Assert.assertEquals(
            type,
            sortTypeCaptor.allValues[0]
        )

        verify(listObserver, times(3)).onChanged(listCaptor.capture())
        Assert.assertEquals(
            GalleryViewModel.ListUI.LoadingShown,
            listCaptor.allValues[0]
        )

        //Since PXLPhoto class is not testable class, this code only verify idx
        Assert.assertEquals(
            GalleryViewModel.ListUI.Data(firstItems).list[0].idx,
            (listCaptor.allValues[1] as GalleryViewModel.ListUI.Data).list[0].idx
        )

        Assert.assertEquals(
            GalleryViewModel.ListUI.Data(secondItems).list[0].idx,
            (listCaptor.allValues[2] as GalleryViewModel.ListUI.Data).list[0].idx
        )

        verify(loadMoreObserver, times(5)).onChanged(loadMoreCaptor.capture())
        Assert.assertEquals(false, loadMoreCaptor.allValues[0])
        Assert.assertEquals(false, loadMoreCaptor.allValues[1])
        Assert.assertEquals(false, loadMoreCaptor.allValues[2])
        Assert.assertEquals(false, loadMoreCaptor.allValues[3])
        Assert.assertEquals(true, loadMoreCaptor.allValues[4])
    }

}