package com.cabbage.fireticv2.presentation.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.cabbage.fireticv2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity(),
                     MainContract.View {

    lateinit private var mPresenter: MainContract.Presenter
    lateinit private var mViewModel: MainViewModel

    @OnClick(R.id.btn_menu_about)
    fun onClick(v: View) {
        mPresenter.requestVersionInfo(v.context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        mPresenter = MainPresenter(this)

        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mViewModel.getCurrentUser().observe(this, Observer<FirebaseUser> { firebaseUser ->
            Timber.i("$firebaseUser")
            if (firebaseUser == null) {
                mPresenter.signInAnonymously()

            }
        })
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
