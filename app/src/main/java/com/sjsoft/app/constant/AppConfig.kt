package com.sjsoft.app.constant

import com.sjsoft.app.BuildConfig

class AppConfig {
    companion object {
        const val prod = "prod"
        const val dev = "dev"
        const val stg = "stg"
        const val mock = "mock"

        fun needDebugInfo(): Boolean {
            return !isProductionVersion || BuildConfig.DEBUG
        }

        val isProductionVersion: Boolean
            get() = BuildConfig.FLAVOR.contains(prod) && !BuildConfig.DEBUG

        val isDevVersion: Boolean
            get() = BuildConfig.FLAVOR.contains(dev)

        val isStgVersion: Boolean
            get() = BuildConfig.FLAVOR.contains(stg)

        val isMockVersion: Boolean
            get() = BuildConfig.FLAVOR.contains(mock)


        const val splashDelay = 3000L
        const val enteringDelay = 400L

        //Pixlee
        const val albumId = "5984962"
        const val LIST_VISIBLE_THRESHOLD = 5
        const val pixleeEmail = "sdk@pixleeteam.com"
        const val pixleeUserName = "SDK Project"

        fun getS3Url(key: String): String {
            return "${BuildConfig.AWS_S3_DOMAIN}$key"
        }

    }

    object DeepLinkParams {
        const val drwNo = "drwNo"
    }
}