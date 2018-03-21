package com.cabbage.fireticv2.presentation.home

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.presentation.base.BaseActivity
import com.cabbage.fireticv2.presentation.utils.toast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.btn_new_game.*
import kotlinx.android.synthetic.main.include_appbar_basic.*
import kotlinx.android.synthetic.main.include_game_options.*

@Suppress("MemberVisibilityCanBePrivate")
class HomeActivity : BaseActivity() {

    //region Bottom Sheet

    private val mBottomSheetBehavior by lazy { BottomSheetBehavior.from(bottomSheetNewGame) }

    private val mBottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
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

    //endregion

    private var mViewModel: HomeViewModel? = null
    private var mDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpActionBar(toolbar)
        setUpBottomSheet()

        val factory = activityComponent.myViewModelFactory()
        mViewModel = ViewModelProviders.of(this, factory)
                .get(HomeViewModel::class.java)

        btnLocalGame.setOnClickListener {
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            mDialog?.dismiss()
            mDialog = HomeActivity.showConfirmDialog(this) { dialog ->
                mViewModel?.newLocalGame()
                dialog.dismiss()
            }
        }

        btnNewSoloGame.setOnClickListener { this.toast("TODO") }
        btnNewOnlineGame.setOnClickListener { this.toast("TODO") }
    }

    override fun onStop() {
        super.onStop()
        mDialog?.dismiss()
        mDialog = null
    }

    override fun setUpActionBar(toolbar: Toolbar) {
        super.setUpActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        LayoutInflater.from(this).inflate(R.layout.btn_new_game, toolbar, true)
        btnNewGame.setOnClickListener { _ -> mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED }
    }

    private fun setUpBottomSheet() {
        mBottomSheetBehavior.state = mBottomSheetState
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback)
        overlay.setOnTouchListener { _, _ ->
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            return@setOnTouchListener true
        }
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

    companion object {
        fun showConfirmDialog(context: Context, onPositive: (dialog: MaterialDialog) -> Unit) =
                MaterialDialog.Builder(context)
                        .title(R.string.dialog_title_reset_game)
                        .content(R.string.dialog_content_reset_game)
                        .positiveText(android.R.string.yes)
                        .negativeText(android.R.string.no)
                        .onPositive { dialog, _ -> onPositive(dialog) }
                        .onNegative { dialog, _ -> dialog.dismiss() }
                        .show()!!
    }
}