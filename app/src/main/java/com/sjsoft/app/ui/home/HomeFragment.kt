package com.sjsoft.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sjsoft.app.R
import com.sjsoft.app.ui.BaseFragment
import com.sjsoft.app.ui.gallery.GalleryFragment
import com.sjsoft.app.ui.upload.UploadFragment
import com.sjsoft.app.util.addFragmentToActivity
import com.sjsoft.app.util.setSafeOnClickListener
import kotlinx.android.synthetic.main.fragment_menu.*

class HomeFragment : BaseFragment(){
    override val titleResource: Int
        get() = R.string.title_main

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bt_gallery.setSafeOnClickListener {
            addFragmentToActivity(GalleryFragment())
        }

        bt_upload.setSafeOnClickListener {
            addFragmentToActivity(UploadFragment())
        }
    }
}