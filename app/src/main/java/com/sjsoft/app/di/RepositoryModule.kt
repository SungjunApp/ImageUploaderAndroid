package com.sjsoft.app.di

import android.content.Context
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.pixlee.pixleesdk.PXLAlbum
import com.sjsoft.app.data.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RepositoryModule {
    @Provides
    fun providePixleeRepository(album: PXLAlbum, awsS3: AmazonS3): PixleeDataSource {
        return PixleeRepository(album, awsS3)
    }

    @Singleton
    @Provides
    fun providePreferenceRepository(context: Context): PreferenceDataSource {
        return PreferenceRepository(context)
    }
}