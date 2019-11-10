package com.sjsoft.app.ui.viewer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.sjsoft.app.R
import com.sjsoft.app.ui.BaseFragment
import com.sjsoft.app.util.gone
import kotlinx.android.synthetic.main.fragment_video_viewer.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoViewerFragment : BaseFragment() {
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
        return inflater.inflate(R.layout.fragment_video_viewer, container, false)
    }

    fun showMMSS(duration: Int, timeInMilli: Int): String {
        val gap = duration - timeInMilli
        val sec = gap / 1000
        val min = sec / 60
        val secOfMin = sec % 60
        return String.format("$min:%02d", secOfMin)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        videoView.setOnPreparedListener {
            v_loading.gone()
            viewLifecycleOwner.lifecycleScope.launch {
                var started = videoView.isPlaying

                while (!started || videoView.isPlaying) {
                    if (!started) {
                        started = videoView.isPlaying
                    }

                    tv_time.text = showMMSS(videoView.duration, videoView.currentPosition)
                    delay(1000)
                }
                tv_time.text = showMMSS(videoView.duration, videoView.duration)
            }
        }
        videoView.setVideoPath(getVideoUrl())
        videoView.start()
    }

    private fun getVideoUrl(): String? {
        return arguments?.getString("videoUrl")
    }

    companion object {
        fun getInstance(videoUrl: String): VideoViewerFragment {
            val f = VideoViewerFragment()
            val bundle = Bundle()
            bundle.putString("videoUrl", videoUrl)
            f.arguments = bundle
            return f
        }
    }
}