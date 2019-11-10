package com.sjsoft.app.di

import android.content.Context
import com.pixlee.pixleesdk.*
import com.sjsoft.app.BuildConfig
import com.sjsoft.app.constant.AppConfig
import dagger.Module
import dagger.Provides

@Module
class PixleeModule{
    @Provides
    //@Singleton
    fun getPXLAlbum(context:Context):PXLAlbum{
        val fo = PXLAlbumFilterOptions()
        fo.minTwitterFollowers = 0
        fo.minInstagramFollowers = 0

        val so = PXLAlbumSortOptions()
        so.sortType = PXLAlbumSortType.RECENCY
        so.descending = true

        val album = PXLAlbum(AppConfig.albumId, context)
        album.setPerPage(40)
        album.setFilterOptions(fo)
        album.setSortOptions(so)

        return album
    }
}