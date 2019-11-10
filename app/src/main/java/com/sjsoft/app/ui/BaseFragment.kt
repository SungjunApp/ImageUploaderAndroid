package com.sjsoft.app.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.sjsoft.app.R
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment() {
    abstract val titleResource: Int

    open fun getCustomTitle(): String? {
        return null
    }

    protected lateinit var disposables: CompositeDisposable

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disposables = CompositeDisposable()
    }

    override fun onDestroyView() {
        val inputManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (activity!!.currentFocus != null && activity!!.currentFocus!!.windowToken != null)
            inputManager.hideSoftInputFromWindow(
                activity!!.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )

        disposables.clear()
        disposables.dispose()

        super.onDestroyView()
    }

    var toast: Toast? = null
    fun showToast(@StringRes message: Int) {
        context?.also { context ->
            toast =
                Toast.makeText(context, message, Toast.LENGTH_LONG)
            toast?.show()
        }
    }

    fun setupConstraintSet(
        existingConstraintSet: ConstraintSet?,
        parentView: ConstraintLayout,
        childView: View,
        showLoadMore: Boolean
    ) :ConstraintSet{
        val constraintSet: ConstraintSet = existingConstraintSet ?: ConstraintSet()

        constraintSet.clone(parentView)

        if (showLoadMore) {
            constraintSet.clear(childView.id, ConstraintSet.TOP)
            constraintSet.connect(
                childView.id, ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID, ConstraintSet.TOP
            )
            constraintSet.connect(
                childView.id, ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
            )
        } else {
            constraintSet.connect(
                childView.id, ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
            )
            constraintSet.clear(childView.id, ConstraintSet.BOTTOM)
        }

        val changeBounds = ChangeBounds()
        changeBounds.duration = 350
        changeBounds.interpolator = AnticipateOvershootInterpolator(1.0f)
        TransitionManager.beginDelayedTransition(parentView, changeBounds)

        constraintSet.applyTo(parentView)
        return constraintSet
    }
}