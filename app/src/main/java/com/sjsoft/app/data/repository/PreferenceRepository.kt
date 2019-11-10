package com.sjsoft.app.data.repository

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

interface PreferenceDataSource{
    fun makeUploadListTapped()
    fun isUploadListTapped():Boolean
    fun makeGalleryListTapped()
    fun isGalleryListTapped():Boolean
}

class PreferenceRepository constructor(val context: Context): PreferenceDataSource{
    val pref:SharedPreferences = context.getSharedPreferences(FileName, Activity.MODE_PRIVATE)

    override fun makeUploadListTapped() {
        val editor = pref.edit()
        editor.putBoolean(keyUploadListTapped, true)
        editor.apply()
    }

    override fun isUploadListTapped():Boolean {
        return pref.getBoolean(keyUploadListTapped, false)
    }

    override fun makeGalleryListTapped() {
        val editor = pref.edit()
        editor.putBoolean(keyGalleryListTapped, true)
        editor.apply()
    }

    override fun isGalleryListTapped():Boolean {
        return pref.getBoolean(keyGalleryListTapped, false)
    }

    companion object{
        const val FileName = "User"
        const val keyGalleryListTapped = "keyGalleryListTapped"
        const val keyUploadListTapped = "keyUploadListTapped" //딥링크를 통해 예약된 조회할 회차번호
    }
}