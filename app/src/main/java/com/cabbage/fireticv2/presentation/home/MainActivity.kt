package com.cabbage.fireticv2.presentation.home

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.cabbage.fireticv2.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
                     MainContract.View {

    lateinit private var mPresenter: MainContract.Presenter

    override fun displayVersionInfo(info: String) {
        Snackbar.make(rootView, info, Snackbar.LENGTH_LONG).show()
    }

    @OnClick(R.id.btn_menu_about)
    fun onClick(v: View) {
        mPresenter.requestVersionInfo(v.context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
    }

    override fun onStart() {
        super.onStart()
        mPresenter = MainPresenter()
        mPresenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.detachView()
    }
}
