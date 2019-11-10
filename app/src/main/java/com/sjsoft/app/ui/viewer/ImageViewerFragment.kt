package com.sjsoft.app.ui.viewer

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.sjsoft.app.GlideApp
import com.sjsoft.app.R
import com.sjsoft.app.ui.BaseFragment
import com.sjsoft.app.util.hide
import kotlinx.android.synthetic.main.fragment_image_viewer.*

class ImageViewerFragment : BaseFragment() {
    override val titleResource: Int
        get() = 0

    override fun getCustomTitle(): String {
        return ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_viewer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*getTransitionName()?.also{
            iv.transitionName = it
        }

        postponeEnterTransition()
        Handler().postDelayed({
            activity?.runOnUiThread {
                Runnable {
                    startPostponedEnterTransition()
                }
            }
        }, 1000)*/

        val requestOptions = RequestOptions()
        requestOptions.centerInside()
        requestOptions.error(R.drawable.baseline_cloud_off_black_48)


        context?.also {
            GlideApp.with(it).asDrawable().clone()
                .load(getImageUrl())
                .thumbnail(0.2f)
                .fitCenter()
                .dontAnimate()
                .apply(requestOptions)
                //.override(Target.SIZE_ORIGINAL)
                //.dontTransform()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        iv.scaleType = ImageView.ScaleType.CENTER
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        v_loading.hide()
                        iv.scaleType = ImageView.ScaleType.FIT_CENTER
                        //startPostponedEnterTransition()
                        return false
                    }
                })
                .into(iv)

            /*GlideApp.with(it)
                .asBitmap()
                .load(getImageUrl())
                .dontAnimate()
                .centerInside()
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val ratio = resource.height.toFloat() / resource.width.toFloat()

                        iv.viewTreeObserver.addOnGlobalLayoutListener(object :
                            ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                try {
                                    if (iv == null)
                                        return

                                    val width = iv.measuredWidth.toFloat()
                                    val height = width * ratio
                                    val params = iv.layoutParams
                                    params.height = height.toInt()
                                    iv.layoutParams = params

                                    //iv.scaleType = ImageView.ScaleType.CENTER_INSIDE
                                    iv.setImageBitmap(resource)
                                    iv.post {
                                        startPostponedEnterTransition()
                                    }

                                    iv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }
                        })


                    }
                })*/

        }

    }

    private fun getImageUrl(): String? {
        return arguments?.getString("imageUrl")
    }

    companion object {
        fun getInstance(imageUrl: String): ImageViewerFragment {
            val f = ImageViewerFragment()
            val bundle = Bundle()
            bundle.putString("imageUrl", imageUrl)
            f.arguments = bundle
            return f
        }
    }
}