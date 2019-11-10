package com.sjsoft.app.di

import com.sjsoft.app.ui.gallery.GalleryFragment
import com.sjsoft.app.ui.upload.UploadFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildModule {
    @ContributesAndroidInjector
    abstract fun contributeHistoryFragment(): GalleryFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): UploadFragment
}