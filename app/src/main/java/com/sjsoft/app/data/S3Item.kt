package com.sjsoft.app.data

import com.amazonaws.services.s3.model.S3ObjectSummary
import com.sjsoft.app.constant.AppConfig

data class S3Item(val contentType: String, val s3Object: S3ObjectSummary) {
    fun isVideo(): Boolean {
        return contentType.toLowerCase().indexOf("video") > -1
    }

    fun getS3Url(): String {
        return AppConfig.getS3Url(s3Object.key)
    }
}