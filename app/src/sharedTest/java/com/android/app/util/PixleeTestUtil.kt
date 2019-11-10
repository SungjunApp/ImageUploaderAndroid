package com.android.app.util

import com.sjsoft.app.data.PXLPhotoItem


class PixleeTestUtil {
    companion object {
        fun getPhotoItems(idx:Int=0): ArrayList<PXLPhotoItem> {
            return arrayListOf(
                PXLPhotoItem(
                    idx,
                    null
                )
            )
        }
    }
}