package com.sjsoft.app.di

import android.content.Context
import com.amazonaws.services.s3.AmazonS3
import com.pixlee.pixleesdk.PXLPdpAlbum
import com.sjsoft.app.data.repository.PixleeDataSource
import com.sjsoft.app.data.repository.PixleeRepository
import com.sjsoft.app.data.repository.PreferenceDataSource
import com.sjsoft.app.data.repository.PreferenceRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RepositoryModule {
    @Provides
    fun providePixleeRepository(album: PXLPdpAlbum, awsS3: AmazonS3): PixleeDataSource {
        return PixleeRepository(album, awsS3)
    }

    @Singleton
    @Provides
    fun providePreferenceRepository(context: Context): PreferenceDataSource {
        return PreferenceRepository(context)
    }
}