package com.cabbage.fireticv2.presentation.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.presentation.gameboard.GameboardActivity
import com.cabbage.fireticv2.presentation.stats.StatsActivity
import com.cabbage.fireticv2.presentation.utils.ViewUtil
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_appbar_collapsing.*
import timber.log.Timber


class MainActivity : AppCompatActivity(),
                     MainContract.View {

    lateinit private var mPresenter: MainContract.Presenter
    lateinit private var mViewModel: MainViewModel

    @OnClick(R.id.btn_menu_online)
    fun onClickO(v: View) {
        if (lastAddedId == null) return

        val db = FirebaseFirestore.getInstance()
        db.collection("rooms").document(lastAddedId!!)
                .get()
                .addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        Timber.d("${it.result.data}")
                    } else {
                        Timber.e(it.exception)
                    }
                }
    }

    private var lastAddedId: String? = null

    @OnClick(R.id.btn_menu_about)
    fun onClick(v: View) {

        val user = HashMap<String, Any>()
        user["first"] = "Ada"
        user["last"] = "Lovelace"
        user["born"] = 1815

        val db = FirebaseFirestore.getInstance()
        db.collection("rooms")
                .add(user)
                .addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        val result = it.result
                        lastAddedId = it.result.id
                        Timber.d(result.id)
                    } else {
                        Timber.e(it.exception)
                    }
                }


        //        mPresenter.requestVersionInfo(v.context)
    }

    @OnClick(R.id.btn_menu_solo)
    fun soloOnClick(v: View) {
        val intent = Intent(v.context, GameboardActivity::class.java)
        startActivity(intent)
    }

    @OnClick(R.id.btn_menu_statistics)
    fun statsOnClick(v: View) {
        val intent = Intent(v.context, StatsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

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

        //        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
        //            Timber.d("$verticalOffset, ${appBarLayout.totalScrollRange}")
        //            if (-verticalOffset > 0.5 * appBarLayout.totalScrollRange) {
        //                ViewCompat.setElevation(appBarLayout, ViewUtil.dpToPixel(this, 4))
        //            } else {
        //                ViewCompat.setElevation(appBarLayout, ViewUtil.dpToPixel(this, 0))
        //            }
        //        }

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
