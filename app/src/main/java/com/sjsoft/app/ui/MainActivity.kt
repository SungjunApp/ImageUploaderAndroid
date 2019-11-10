package com.sjsoft.app.ui

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sjsoft.app.BuildConfig
import com.sjsoft.app.R
import com.sjsoft.app.constant.AppConfig
import com.sjsoft.app.ui.home.HomeFragment
import com.sjsoft.app.util.*
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    val frameLayoutId = R.id.contentFrame

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.navigationIcon?.setColorFilter(
            ContextCompat.getColor(this, R.color.grey_60),
            PorterDuff.Mode.SRC_ATOP
        )
        setSystemBarColor(R.color.grey_5)
        setSystemBarLight()

        if (AppConfig.needDebugInfo()) {
            toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            toolbar.setTitleTextAppearance(this, R.style.TextAppearance_AppCompat_Medium)
            toolbar.setSubtitleTextAppearance(this, R.style.TextAppearance_AppCompat_Caption)
        }

        supportFragmentManager.addOnBackStackChangedListener(mOnBackStackChangedListener)

        replaceFragmentInActivity(frameLayoutId, HomeFragment())
    }

    private val mOnBackStackChangedListener = FragmentManager.OnBackStackChangedListener {
        try {
            var showToolbar = true
            val fragmentCount = supportFragmentManager.backStackEntryCount
            val title: String?
            title =
                if (fragmentCount > 0) {
                    val fragment =
                        supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1] as BaseFragment
                    if (fragment is HomeFragment) showToolbar = false

                    fragment.getCustomTitle() ?: getString(fragment.titleResource)
                } else
                    getString(R.string.app_name)

//            toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
//            toolbar.setTitleTextAppearance(this, R.style.TextAppearance_AppCompat_Medium)
//            toolbar.setSubtitleTextAppearance(this, R.style.TextAppearance_AppCompat_Caption)

            Timber.d("fragmentCount: $fragmentCount")
            setupActionBar(toolbar) {
                //if (showToolbar) show() else hide()
                setTitle(title)
                setDisplayHomeAsUpEnabled(fragmentCount > 1)
                setDisplayShowHomeEnabled(fragmentCount > 1)
            }

            if (AppConfig.needDebugInfo())
                supportActionBar?.subtitle =
                    "Bucket: ${BuildConfig.AWS_S3_BUCKET_NAME},\tVer: ${getAppVersionName()}"

            invalidateOptionsMenu()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        try {
            val fragmentStackSize = supportFragmentManager.backStackEntryCount
            if (fragmentStackSize <= 1) {
                finish()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getCurrentFragment()?.also {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getCurrentFragment()?.also {
            it.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}
