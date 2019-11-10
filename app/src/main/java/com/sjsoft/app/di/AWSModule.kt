package com.sjsoft.app.di

import com.amazonaws.ClientConfiguration
import com.amazonaws.Protocol
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.sjsoft.app.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AWSModule {
    @Provides
    @Singleton
    fun provideBasicAWSCredentials(): AWSCredentials {
        return BasicAWSCredentials(BuildConfig.AWS_ACCESS_KEY, BuildConfig.AWS_SECRET_KEY)
    }

    @Provides
    @Singleton
    fun provideAmazonS3(awsCreds: AWSCredentials): AmazonS3 {
        val clientConfig = ClientConfiguration()
        clientConfig.protocol = Protocol.HTTP
        val conn = AmazonS3Client(awsCreds, clientConfig)
        conn.setRegion(Region.getRegion(BuildConfig.AWS_REGION))
        return conn
    }
}