package com.sjsoft.app.di

import com.sjsoft.app.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuildModule {
    @ContributesAndroidInjector(modules = [FragmentBuildModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
