package com.sjsoft.app.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pixlee.pixleesdk.PXLAlbumSortType
import com.sjsoft.app.R
import com.sjsoft.app.di.Injectable
import com.sjsoft.app.ui.BaseFragment
import com.sjsoft.app.ui.holders.MarginInfo
import com.sjsoft.app.ui.viewer.ImageViewerFragment
import com.sjsoft.app.util.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.bt_more
import kotlinx.android.synthetic.main.fragment_list.recyclerView
import kotlinx.android.synthetic.main.fragment_list.v_content_box
import kotlinx.android.synthetic.main.fragment_list.v_loading
import kotlinx.android.synthetic.main.fragment_list.v_shadow
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import javax.inject.Inject

class GalleryFragment : BaseFragment(), Injectable {
    override val titleResource: Int
        get() = R.string.title_gallery

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: GalleryViewModel by viewModels {
        viewModelFactory
    }

    val adapter: GalleryAdapter by lazy {
        GalleryAdapter(
            context!!,
            MarginInfo(
                0
                , 3.toPx()
                , 3.toPx()
            )
        ) {
            moveToViewer(it)
        }
    }

    fun moveToViewer(image: String) {
        addFragmentToActivity(ImageViewerFragment.getInstance(image))
    }

    internal var gridLayoutManager: GridLayoutManager? = null
    var GRID_COUNT: Int = 3

    val constraintSet = ConstraintSet()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.setPaddingRelative(
            3.toPx()
            , 10.toPx()
            , 3.toPx()
            , 50.toPx()
        )

        recyclerView.setShadowViewController(v_shadow)

        viewModel.sortType.observe(this, Observer {
            changeTextViewColor(tab_recency, it, PXLAlbumSortType.RECENCY)
            changeTextViewColor(tab_random, it, PXLAlbumSortType.RANDOM)
            changeTextViewColor(tab_pixlee_shares, it, PXLAlbumSortType.PIXLEE_SHARES)
            changeTextViewColor(tab_pixlee_likes, it, PXLAlbumSortType.PIXLEE_LIKES)
            changeTextViewColor(tab_pixlee_popularity, it, PXLAlbumSortType.POPULARITY)
            changeTextViewColor(tab_pixlee_dynamic, it, PXLAlbumSortType.DYNAMIC)
        })


        viewModel.loadMoreUI.observe(this, Observer {
            constraintSet.setupLoadMore(v_content_box, bt_more, it)
        })
        viewModel.listUI.observe(this, Observer {

            when (it) {
                is GalleryViewModel.ListUI.LoadingShown -> {
                    v_loading.show()
                    adapter.submitList(null)
                }
                is GalleryViewModel.ListUI.LoadingHide -> {
                    v_loading.hide()
                }
                is GalleryViewModel.ListUI.Data -> {
                    v_loading.hide()
                    adapter.submitList(it.list)
                }

            }
        })

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (itemCount > 0 && !viewModel.isListTapped()) {
                    recyclerView.post {
                        MaterialTapTargetPrompt.Builder(activity!!)
                            .setTarget(R.id.v_root_gallery)
                            .setPrimaryText(getString(R.string.title_guide_viewer))
                            .setSecondaryText(getString(R.string.message_guide_list))
                            .setPromptStateChangeListener { prompt, state ->
                                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                    viewModel.makeListTapped()
                                    adapter.currentList[0]?.photo?.cdnLargeUrl?.toString()
                                        ?.also { moveToViewer(it) }
                                }
                            }
                            .show()
                    }
                }
            }
        })

        tab_recency.setOnClickListener {
            viewModel.changeTab(viewModel.generateSortOption(PXLAlbumSortType.RECENCY))
        }

        tab_random.setOnClickListener {
            viewModel.changeTab(viewModel.generateSortOption(PXLAlbumSortType.RANDOM))
        }

        tab_pixlee_shares.setOnClickListener {
            viewModel.changeTab(viewModel.generateSortOption(PXLAlbumSortType.PIXLEE_SHARES))
        }

        tab_pixlee_likes.setOnClickListener {
            viewModel.changeTab(viewModel.generateSortOption(PXLAlbumSortType.PIXLEE_LIKES))
        }

        tab_pixlee_popularity.setOnClickListener {
            viewModel.changeTab(viewModel.generateSortOption(PXLAlbumSortType.POPULARITY))
        }

        tab_pixlee_dynamic.setOnClickListener {
            viewModel.changeTab(viewModel.generateSortOption(PXLAlbumSortType.DYNAMIC))
        }

        bt_more.setOnClickListener {
            viewModel.loadMore()
        }

        gridLayoutManager = GridLayoutManager(context, GRID_COUNT)
        recyclerView!!.layoutManager = gridLayoutManager
        gridLayoutManager?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }

        recyclerView.adapter = adapter
        setupScrollListener()

        if (viewModel.listUI.value == null) {
            viewModel.changeTab()
        }
    }

    fun changeTextViewColor(
        textView: TextView,
        currentType: PXLAlbumSortType,
        targetType: PXLAlbumSortType
    ) {
        val color = if (currentType == targetType) R.color.active else R.color.inactive
        textView.setTextColor(ContextCompat.getColor(textView.context, color))
    }

    private fun setupScrollListener() {
        recyclerView.addOnScrollListener(object :
            androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                gridLayoutManager?.apply {
                    viewModel.listScrolled(childCount, findLastVisibleItemPosition(), itemCount)
                }

            }
        })
    }
}