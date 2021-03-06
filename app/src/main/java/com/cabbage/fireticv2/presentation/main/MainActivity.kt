package com.cabbage.fireticv2.presentation.main

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.presentation.base.BaseActivity
import com.cabbage.fireticv2.presentation.roomlist.RoomListActivity
import com.cabbage.fireticv2.presentation.stats.StatsActivity
import com.cabbage.fireticv2.presentation.utils.ViewUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_appbar_collapsing.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(),
                     MainContract.View {

    @Inject lateinit var mViewModel: MainViewModel
    @Inject lateinit var mPresenter: MainContract.Presenter

    @OnClick(R.id.btn_menu_online)
    fun onClickO(v: View) {
        val intent = Intent(v.context, RoomListActivity::class.java)
        startActivity(intent)
    }

    @OnClick(R.id.btn_menu_about)
    fun onClick(v: View) {
        mPresenter.requestVersionInfo(v.context)
    }

    @OnClick(R.id.btn_menu_solo)
    fun soloOnClick(v: View) {

    }

    @OnClick(R.id.btn_menu_statistics)
    fun statsOnClick(v: View) {
        val intent = Intent(v.context, StatsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        configureAppBar()

//        activityComponent.inject(this)

        mViewModel.firebaseUser().observe(this, Observer { firebaseUser ->
            when (firebaseUser) {
                null -> mPresenter.signInAnonymously()
                else -> mPresenter.checkIfUserExistsInFirestore(firebaseUser)
            }
        })

//        mPresenter.signInAnonymously()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.v("onDestroy")
    }

    private fun configureAppBar() {
        // Compensate for translucent status bar
        ViewUtil.getStatusBarHeight(this)?.let { statusBarHeight ->
            toolbar.setPadding(
                    toolbar.paddingLeft,
                    toolbar.paddingTop + statusBarHeight,
                    toolbar.paddingRight,
                    toolbar.paddingBottom)

            toolbar.layoutParams.height += statusBarHeight
            appBarLayout.layoutParams.height += statusBarHeight
        }

        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            //            Timber.v("$verticalOffset, ${appBarLayout.totalScrollRange}")

            val elevation = if (-verticalOffset > 0.5 * appBarLayout.totalScrollRange) 4 else 0
            ViewCompat.setElevation(appBarLayout, ViewUtil.dpToPixel(this, elevation))
        }
    }

    override fun onStart() {
        super.onStart()
        mPresenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.detachView()
    }

    override fun displayVersionInfo(info: String) {
        Snackbar.make(rootView, info, Snackbar.LENGTH_LONG).show()
    }

    override fun toastMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
