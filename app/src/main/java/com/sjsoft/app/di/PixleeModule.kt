package com.sjsoft.app.di

import android.content.Context
import com.pixlee.pixleesdk.*
import com.sjsoft.app.BuildConfig
import com.sjsoft.app.constant.AppConfig
import dagger.Module
import dagger.Provides

@Module
class PixleeModule {
    @Provides
    //@Singleton
    fun getPXLAlbum(context: Context): PXLBaseAlbum {
        PXLClient.initialize(BuildConfig.PIXLEE_API_KEY, BuildConfig.PIXLEE_SECRET_KEY)

        val fo = PXLAlbumFilterOptions()
        fo.minTwitterFollowers = 0
        fo.minInstagramFollowers = 0

        val so = PXLAlbumSortOptions()
        so.sortType = PXLAlbumSortType.RECENCY
        so.descending = true


//        val album = PXLAlbum(AppConfig.albumId, PXLClient.getInstance(context).basicrepo, PXLClient.getInstance(context).analyticsRepo)
        val album = PXLPdpAlbum("932720", PXLClient.getInstance(context).basicrepo, PXLClient.getInstance(context).analyticsRepo)
        album.setPerPage(40)
        album.setFilterOptions(fo)
        album.setSortOptions(so)

        return album as PXLBaseAlbum
    }

    @Provides
    //@Singleton
    fun getPXLAnalytics(context: Context): PXLAnalytics {
        PXLClient.initialize(BuildConfig.PIXLEE_API_KEY, BuildConfig.PIXLEE_SECRET_KEY)
        val analytics = PXLAnalytics(PXLClient.getInstance(context).analyticsRepo)
        return analytics
    }

}