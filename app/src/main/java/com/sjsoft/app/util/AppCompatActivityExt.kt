package com.sjsoft.app.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sjsoft.app.R
import com.sjsoft.app.ui.BaseFragment

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(
    frameId: Int,
    fragment: Fragment,
    sharedView: View? = null
) {
    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    supportFragmentManager.transact {
        if (sharedView != null)
            addSharedElement(sharedView, sharedView.transitionName)
        else{
            setCustomAnimations(0, 0, 0, 0)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        addToBackStack(fragment.getIdentifier())
        replace(frameId, fragment, fragment.getIdentifier())
    }
}

/**
 * The `fragment` is added to the container view with proposalIndex. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentToActivity(
    frameId: Int, fragment: Fragment,
    sharedView: View? = null
) {
    supportFragmentManager.transact {
        if (sharedView != null)
            addSharedElement(sharedView, sharedView.transitionName)
        else {
            setCustomAnimations(
                R.anim.slide_in_right_left,
                R.anim.slide_out_right_left,
                R.anim.slide_in_left_right,
                R.anim.slide_out_left_right
            )
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        addToBackStack(fragment.getIdentifier())
        replace(frameId, fragment, fragment.getIdentifier())
    }
}

/**
 * The `fragment` is added to the container view with proposalIndex. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.popAndAddFragmentToActivity(
    frameId: Int, fragment: Fragment,
    sharedView: View? = null
) {
    supportFragmentManager.popBackStack()

    supportFragmentManager.transact {
        if (sharedView != null)
            addSharedElement(sharedView, sharedView.transitionName)
        else {
//            setCustomAnimations(
//                R.anim.slide_in_right_left,
//                R.anim.slide_out_right_left,
//                R.anim.slide_in_left_right,
//                R.anim.slide_out_left_right
//            )
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        addToBackStack(fragment.getIdentifier())
        replace(frameId, fragment, fragment.getIdentifier())
    }
}

/**
 * The `fragment` is restart on the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.restartFragmentByTag(fragmentTag: String) {
    supportFragmentManager.popBackStack()
    val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
    fragment?.let {
        supportFragmentManager.transact {
            detach(it)
            attach(it)
        }
    }
}

fun AppCompatActivity.setupActionBar(toolbar: Toolbar, action: ActionBar.() -> Unit) {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        action()
    }
}

fun AppCompatActivity.sendTextIntent(title: String, contents: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TITLE, title)
        putExtra(Intent.EXTRA_TEXT, contents)
        type = "text/plain"
    }
    startActivity(
        Intent.createChooser(
            sendIntent,
            title
        )
    )
}

fun AppCompatActivity.getCurrentFragment(): BaseFragment? {
    val fragmentCount = supportFragmentManager.backStackEntryCount
    if (fragmentCount > 0) {
        val backEntry = supportFragmentManager.getBackStackEntryAt(fragmentCount - 1)
        val f = supportFragmentManager.findFragmentByTag(backEntry.name)
        return if (f != null) f as BaseFragment
        else null
    }

    return null
}

fun AppCompatActivity.goToRootFragment() {
    val count = supportFragmentManager.backStackEntryCount
    if (count >= 2) {
        val be = supportFragmentManager.getBackStackEntryAt(0)
        supportFragmentManager.popBackStack(be.id, 0)
    }
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}


fun AppCompatActivity.setSystemBarColor() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
    }
}

fun AppCompatActivity.setSystemBarColor(@ColorRes colorRes: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, colorRes)
    }
}

fun AppCompatActivity.setSystemBarColorInt(@ColorRes colorRes: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, colorRes)
    }
}

fun AppCompatActivity.setSystemBarColorDialog(dialog: Dialog, @ColorRes colorRes: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = dialog.window
        window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, colorRes)
    }
}

fun AppCompatActivity.setSystemBarLight() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = findViewById<View>(android.R.id.content)
        var flags = view.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        view.systemUiVisibility = flags
    }
}

fun AppCompatActivity.setSystemBarLightDialog(dialog: Dialog) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = dialog.findViewById<View>(android.R.id.content)
        var flags = view.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        view.systemUiVisibility = flags
    }
}

fun AppCompatActivity.clearSystemBarLight() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
    }
}

/**
 * Making notification bar transparent
 */
fun AppCompatActivity.setSystemBarTransparent(act: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = act.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }
}

fun Context.getAppVersionCode(): Long {
    val pInfo = packageManager.getPackageInfo(packageName, 0)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        pInfo.longVersionCode/* - 1*/
    else
        pInfo.versionCode.toLong()/* - 1*/
}

fun Context.getAppVersionName(): String {
    val pInfo = packageManager.getPackageInfo(packageName, 0)
    return pInfo.versionName
}