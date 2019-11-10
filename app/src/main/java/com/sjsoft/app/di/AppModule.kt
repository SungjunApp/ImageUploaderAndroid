package com.sjsoft.app.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
abstract class AppModule {
    @Singleton
    @Binds
    abstract fun bindContext(application: Application): Context
}