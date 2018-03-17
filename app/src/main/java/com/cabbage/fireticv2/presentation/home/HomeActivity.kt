package com.cabbage.fireticv2.presentation.home

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.btn_new_game.*
import kotlinx.android.synthetic.main.include_appbar_basic.*
import kotlinx.android.synthetic.main.include_game_options.*

class HomeActivity : BaseActivity() {

    private val mBottomSheetBehavior by lazy { BottomSheetBehavior.from(bottomSheetNewGame) }

    private val mBottomSheetCallback =
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                    Timber.v("slideOffset $slideOffset")
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (mBottomSheetState != newState) {
                        when (mBottomSheetState) {
                            BottomSheetBehavior.STATE_EXPANDED -> {
                                overlay?.visibility = View.GONE
                            }
                            BottomSheetBehavior.STATE_HIDDEN -> {
                                overlay?.visibility = View.VISIBLE
                            }
                        }
                    }
                    mBottomSheetState = newState
                }
            }

    private var mBottomSheetState = BottomSheetBehavior.STATE_HIDDEN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpActionBar(toolbar)

        mBottomSheetBehavior.state = mBottomSheetState
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback)
        overlay.setOnTouchListener { _, _ ->
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            return@setOnTouchListener true
        }

        LayoutInflater.from(this).inflate(R.layout.btn_new_game, toolbar, true)
        btnNewGame.setOnClickListener { _ -> mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED }

//        ButterKnife.bind(this)
    }

    override fun setUpActionBar(toolbar: Toolbar) {
        super.setUpActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onBackPressed() {
        if (mBottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.about -> {
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                val strVersion = "${packageInfo.versionName} (${packageInfo.versionCode})"
                Toast.makeText(this, strVersion, Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}