package com.sjsoft.app.util

import android.content.DialogInterface
import android.transition.Fade
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sjsoft.app.R
import com.sjsoft.app.ui.MainActivity
import com.sjsoft.app.view.DetailsTransition

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun Fragment.replaceIntoParentFragment(frameId: Int, fragment: Fragment) {
    childFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    childFragmentManager.transact {
        Log.e("replFragment", "fragment.getIdentifier():${fragment.getIdentifier()}")
        setCustomAnimations(0, 0, 0, 0)
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        addToBackStack(fragment.getIdentifier())
        replace(frameId, fragment, fragment.getIdentifier())
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun Fragment.addIntoParentFragment(frameId: Int, fragment: Fragment) {
    childFragmentManager.transact {
        setCustomAnimations(
            R.anim.slide_in_right_left,
            R.anim.slide_out_right_left,
            R.anim.slide_in_left_right,
            R.anim.slide_out_left_right
        )
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        addToBackStack(fragment.getIdentifier())
        replace(frameId, fragment, fragment.getIdentifier())
    }
}

fun Fragment.replaceFragmentInActivity(fragment: Fragment, sharedView: View? = null) {
    if (activity is MainActivity) {
        val mainActivity = activity as MainActivity
        if(sharedView!=null){
            fragment.sharedElementEnterTransition = DetailsTransition()
            fragment.enterTransition = Fade()
            exitTransition = Fade()
            fragment.sharedElementReturnTransition = DetailsTransition()
        }

        mainActivity.replaceFragmentInActivity(mainActivity.frameLayoutId, fragment, sharedView)
    } else {
        Toast.makeText(context, "액티비티에 replaceFragmentInActivity() 함수 개발필요", Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.addFragmentToActivity(fragment: Fragment, sharedView: View? = null) {
    if (activity is MainActivity) {
        val mainActivity = activity as MainActivity

        if(sharedView!=null){
            fragment.sharedElementEnterTransition = DetailsTransition()
            fragment.enterTransition = Fade()
            exitTransition = Fade()
            fragment.sharedElementReturnTransition = DetailsTransition()
        }

        mainActivity.addFragmentToActivity(mainActivity.frameLayoutId, fragment, sharedView)
    } else {
        Toast.makeText(context, "액티비티에 addFragmentToActivity() 함수 개발필요", Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.popAndAddFragmentToActivity(fragment: Fragment, sharedView: View? = null) {
    if (activity is MainActivity) {
        val mainActivity = activity as MainActivity

        if(sharedView!=null){
            fragment.sharedElementEnterTransition = DetailsTransition()
            fragment.enterTransition = Fade()
            exitTransition = Fade()
            fragment.sharedElementReturnTransition = DetailsTransition()
        }

        mainActivity.popAndAddFragmentToActivity(mainActivity.frameLayoutId, fragment, sharedView)
    } else {
        Toast.makeText(context, "액티비티에 popAndAddFragmentToActivity() 함수 개발필요", Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.restartFragmentByTag(fragmentTag: String) {
    if (activity is MainActivity) {
        val mainActivity = activity as MainActivity
        mainActivity.restartFragmentByTag(fragmentTag)
    } else {
        Toast.makeText(context, "액티비티에 restartFragmentByTag() 함수 개발필요", Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.goToRootFragment() {
    if (activity is MainActivity) {
        val mainActivity = activity as MainActivity
        mainActivity.goToRootFragment()
    } else {
        Toast.makeText(context, "액티비티에 goToRootFragment() 함수 개발필요", Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.onBackPressed() {
    if (activity is MainActivity) {
        val mainActivity = activity as MainActivity
        mainActivity.onBackPressed()
    } else {
        Toast.makeText(context, "액티비티에 onBackPressed() 함수 개발필요", Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.sendTextIntent(title: String, contents: String) {
    if (activity is MainActivity) {
        val mainActivity = activity as MainActivity
        mainActivity.sendTextIntent(title, contents)
    } else {
        Toast.makeText(context, "액티비티에 sendTextIntent() 함수 개발필요", Toast.LENGTH_SHORT).show()
    }
}

/*fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProviders.of(this, ShiftViewModelFactory.getInstance(application)).get(viewModelClass)*/

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun Fragment.getIdentifier(): String {
    return this::class.java.canonicalName ?: ""
}

fun Fragment.showAlertDialog(
    message: CharSequence,
    title: CharSequence? = null,
    onDismiss: DialogInterface.OnDismissListener? = null
) {
    context?.let {
        val builder = AlertDialog.Builder(it)
        title?.let { builder.setTitle(title) }
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
        }
        builder.setOnDismissListener(onDismiss)
        builder.show()
    }
}