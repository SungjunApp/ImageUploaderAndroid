package com.sjsoft.app.ui.upload

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sjsoft.app.constant.AppConfig
import com.sjsoft.app.data.S3Item
import com.sjsoft.app.ui.holders.GridViewHolder
import com.sjsoft.app.ui.holders.MarginInfo
import com.sjsoft.app.util.setSafeOnClickListener

class UploadAdapter(
    val context: Context,
    private val marginInfo: MarginInfo,
    val clickListener: (S3Item) -> Unit
) :
    ListAdapter<S3Item, GridViewHolder>(REPO_COMPARATOR) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GridViewHolder {
        return GridViewHolder.create(viewGroup)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item.getS3Url(), item.isVideo(), marginInfo)
        holder.itemView.setSafeOnClickListener {
            clickListener(item)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<S3Item>() {
            override fun areItemsTheSame(
                oldItem: S3Item,
                newItem: S3Item
            ): Boolean =
                oldItem.s3Object.key == newItem.s3Object.key

            override fun areContentsTheSame(
                oldItem: S3Item,
                newItem: S3Item
            ): Boolean =
                oldItem.s3Object.key == newItem.s3Object.key
        }
    }
}