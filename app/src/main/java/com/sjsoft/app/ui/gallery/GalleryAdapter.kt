package com.sjsoft.app.ui.gallery

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pixlee.pixleesdk.PXLPhotoSize
import com.sjsoft.app.GlideApp
import com.sjsoft.app.R
import com.sjsoft.app.constant.AppConfig
import com.sjsoft.app.data.PXLPhotoItem
import com.sjsoft.app.ui.holders.GridViewHolder
import com.sjsoft.app.ui.holders.MarginInfo
import com.sjsoft.app.util.setSafeOnClickListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_gallery.*

class GalleryAdapter(
    val context: Context,
    private val marginInfo: MarginInfo,
    val clickListener: (String) -> Unit
) :
    ListAdapter<PXLPhotoItem, GridViewHolder>(REPO_COMPARATOR) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GridViewHolder {
        return GridViewHolder.create(viewGroup)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val imageUrl = getItem(position)?.photo?.getUrlForSize(PXLPhotoSize.MEDIUM)?.toString()

        holder.bind(imageUrl, false, marginInfo)
        holder.itemView.setSafeOnClickListener {
            getItem(position)?.photo?.getUrlForSize(PXLPhotoSize.BIG)?.toString()?.also { clickListener(it) }
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<PXLPhotoItem>() {
            override fun areItemsTheSame(oldItem: PXLPhotoItem, newItem: PXLPhotoItem): Boolean =
                oldItem.idx == newItem.idx

            override fun areContentsTheSame(oldItem: PXLPhotoItem, newItem: PXLPhotoItem): Boolean =
                oldItem.idx == newItem.idx
        }
    }
}