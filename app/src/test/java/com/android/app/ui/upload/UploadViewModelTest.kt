package com.android.app.ui.upload

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.android.app.rule.CoroutinesTestRule
import com.sjsoft.app.R
import com.sjsoft.app.data.S3Item
import com.sjsoft.app.data.repository.PixleeDataSource
import com.sjsoft.app.data.repository.PreferenceDataSource
import com.sjsoft.app.data.repository.UploadInfo
import com.sjsoft.app.ui.upload.UploadViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class UploadViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var pixlee: PixleeDataSource

    @Mock
    lateinit var pref: PreferenceDataSource

    private lateinit var viewModel: UploadViewModel

    @Mock
    private lateinit var listObserver: Observer<UploadViewModel.ListUI>

    @Captor
    private lateinit var listCaptor: ArgumentCaptor<UploadViewModel.ListUI>

    @Mock
    private lateinit var loadMoreObserver: Observer<Boolean>

    @Captor
    private lateinit var loadMoreCaptor: ArgumentCaptor<Boolean>

    @Mock
    private lateinit var uploadObserver: Observer<UploadViewModel.UploadUI>

    @Captor
    private lateinit var uploadCaptor: ArgumentCaptor<UploadViewModel.UploadUI>



    @ExperimentalCoroutinesApi
    @Before
    fun setupViewModel() {
        MockitoAnnotations.initMocks(this)
        viewModel = UploadViewModel(pixlee, pref)
        viewModel.listUI.observeForever(listObserver)
        viewModel.loadMoreUI.observeForever(loadMoreObserver)
        viewModel.uploadUI.observeForever(uploadObserver)
    }

    @After
    fun after() {
        viewModel.listUI.removeObserver(listObserver)
        viewModel.loadMoreUI.removeObserver(loadMoreObserver)
        viewModel.uploadUI.removeObserver(uploadObserver)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get S3Images succeeded`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val item = S3Item("image", S3ObjectSummary())
        item.s3Object.key = "hello"
        val list = listOf(
            item
        )

        `when`(pixlee.getS3Images()).thenReturn(list)
        viewModel.getS3Images()

        verify(loadMoreObserver, times(2)).onChanged(loadMoreCaptor.capture())
        Assert.assertEquals(false, loadMoreCaptor.allValues[0])
        Assert.assertEquals(false, loadMoreCaptor.allValues[1])

        verify(listObserver, times(2)).onChanged(listCaptor.capture())
        Assert.assertEquals(UploadViewModel.ListUI.LoadingShown, listCaptor.allValues[0])
        Assert.assertEquals(UploadViewModel.ListUI.Data(list), listCaptor.allValues[1])
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get S3Images failed`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        `when`(pixlee.getS3Images()).thenThrow(IllegalArgumentException(""))
        viewModel.getS3Images()

        verify(loadMoreObserver, times(3)).onChanged(loadMoreCaptor.capture())
        Assert.assertEquals(false, loadMoreCaptor.allValues[0])
        Assert.assertEquals(false, loadMoreCaptor.allValues[1])
        Assert.assertEquals(true, loadMoreCaptor.allValues[2])

        verify(listObserver, times(2)).onChanged(listCaptor.capture())
        Assert.assertEquals(UploadViewModel.ListUI.LoadingShown, listCaptor.allValues[0])
        Assert.assertEquals(UploadViewModel.ListUI.LoadingHidden, listCaptor.allValues[1])
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `upload an image succeeded`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val filePath = "image.png"
        val contentType = "image"
        val returnUrl = "http://aws.hihi.com/face.jpg"
        val uploadInfo = UploadInfo(true, returnUrl)
        `when`(pixlee.uploadImage(filePath, contentType)).thenReturn(uploadInfo)

        viewModel.uploadImage(filePath, contentType)

        verify(uploadObserver, times(2)).onChanged(uploadCaptor.capture())
        Assert.assertEquals(UploadViewModel.UploadUI.Uploading, uploadCaptor.allValues[0])
        Assert.assertEquals(UploadViewModel.UploadUI.Complete(returnUrl, R.string.upload_success_message), uploadCaptor.allValues[1])
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `upload an image failed`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val filePath = "image.png"
        val contentType = "image"
        `when`(pixlee.uploadImage(filePath, contentType)).thenThrow(IllegalArgumentException(""))

        viewModel.uploadImage(filePath, contentType)

        verify(uploadObserver, times(2)).onChanged(uploadCaptor.capture())
        Assert.assertEquals(UploadViewModel.UploadUI.Uploading, uploadCaptor.allValues[0])
        Assert.assertEquals(UploadViewModel.UploadUI.Error(R.string.upload_failure_message), uploadCaptor.allValues[1])
    }

}