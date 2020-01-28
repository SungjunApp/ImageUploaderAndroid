package com.sjsoft.app.data.repository

import android.util.Log
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.pixlee.pixleesdk.*
import com.sjsoft.app.BuildConfig
import com.sjsoft.app.constant.AppConfig
import com.sjsoft.app.data.PXLPhotoItem
import com.sjsoft.app.data.S3Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

interface PixleeDataSource {
    fun loadNextPageOfPhotos(options: PXLAlbumSortOptions? = null): Flow<ArrayList<PXLPhotoItem>>
    suspend fun uploadImage(filePath: String, contentType: String): UploadInfo
    suspend fun getS3Images(): List<S3Item>
}

data class UploadInfo(val isComplete: Boolean, val url: String? = null)

class PixleeRepository constructor(
    private val album: PXLPdpAlbum,
    private val analytics: PXLAnalytics,
    private val awsS3: AmazonS3
) : PixleeDataSource {
    override suspend fun getS3Images(): List<S3Item> {
        var list = ArrayList<S3Item>()
        withContext(Dispatchers.IO) {
            val objects = awsS3.listObjects(BuildConfig.AWS_S3_BUCKET_NAME).objectSummaries
            objects.forEach {
                val meta = awsS3.getObjectMetadata(BuildConfig.AWS_S3_BUCKET_NAME, it.key)
                list.add(S3Item(meta.contentType, it))
                //awsS3.deleteObject(BuildConfig.AWS_S3_BUCKET_NAME, it.key)
            }
            list.sortByDescending { it.s3Object.lastModified.time }

        }
        return list
    }

    @ExperimentalCoroutinesApi
    override suspend fun uploadImage(
        filePath: String,
        contentType: String
    ): UploadInfo {
        val keyName = "${AppConfig.pixleeEmail}/${UUID.randomUUID()}"
        var uploadInfo = UploadInfo(false)
        withContext(Dispatchers.IO) {
            val file = File(filePath)
            val request = PutObjectRequest(BuildConfig.AWS_S3_BUCKET_NAME, keyName, file)
            val metadata = ObjectMetadata()
            metadata.contentType = contentType
            request.metadata = metadata
            awsS3.putObject(request)
            awsS3.setObjectAcl(
                BuildConfig.AWS_S3_BUCKET_NAME,
                keyName,
                CannedAccessControlList.PublicRead
            )

            uploadInfo = UploadInfo(
                true,
                url = awsS3.getUrl(BuildConfig.AWS_S3_BUCKET_NAME, keyName).toExternalForm()
            )

            Log.e("PixRepo", "PixRepo.end: ${uploadInfo.url}")
        }

        return uploadInfo
    }

    override fun loadNextPageOfPhotos(options: PXLAlbumSortOptions?): Flow<ArrayList<PXLPhotoItem>> =
        flow {
            val jobFinished = -1
            val jobWorking = 0
            val jobError = 1
            var type = jobWorking

            var remoteResult: List<PXLPhoto>? = null

            options?.also { album.setSortOptions(it) }
            //album.cancellAll()
            album.loadNextPageOfPhotos(object : PXLAlbum.RequestHandlers {
                override fun DataLoadedHandler(photos: java.util.ArrayList<PXLPhoto>?) {
                    remoteResult = photos
                }

                override fun DataLoadFailedHandler(error: String?) {
                    type = jobError
                    Log.e("GalleryVM", "GalleryVM.error: $error")
                    //error(error ?: "data load failed")

                }
            })

            while (type >= jobWorking) {
                delay(700)

                if (type == jobError) {
                    throw IllegalArgumentException()
                }

                remoteResult?.also {
                    var lastIndex = -1
                    val result = ArrayList<PXLPhotoItem>()

                    it.forEach {
                        result.add(PXLPhotoItem(++lastIndex, it))
                    }
                    emit(result)
                    type = jobFinished

                    //album.getPhotoWithId(result.first().photo, null)

                    //album.actionClicked(it.last(), "http://google.com")
                    //it.last().openedLightbox(context)

                }
            }
//            album.openedWidget()
            /*album.uploadImage("seoulTower",
                "sungjun.app@gmail.com",
                "jun",
                "https://www.dragonhilllodge.com/application/files/3215/5479/9457/fall_DHL5.jpg",
                true,
                null
            )*/

            //analytics.addToCart("932720","100,000", 2, "원")
            //analytics.addToCart("932720","100,000", 2)
            val list = ArrayList<HashMap<String, Any>>()
            val map = HashMap<String, Any>()
            map["Name"] = "Jun"
            map["Gender"] = "Male"
            list.add(map)
            //analytics.conversion(list, "30",3)
            album.loadMore()

        }

}