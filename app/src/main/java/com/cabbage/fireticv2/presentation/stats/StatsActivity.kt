package com.cabbage.fireticv2.presentation.stats

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import butterknife.ButterKnife
import com.cabbage.fireticv2.MyApplication
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.presentation.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_stats.*
import kotlinx.android.synthetic.main.include_appbar_basic.*

class StatsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        ButterKnife.bind(this)
        setUpActionBar(toolbar)

        val repo = MyApplication.appComponent.fireTicRepository()
        val factory = FunViewModelProvider(repo)
        val viewModel = ViewModelProviders.of(this, factory).get(FunViewModel::class.java)

        viewModel.getData().observe(this, Observer<Int> {
            tvTest.text = "$it"
        })

        btnRaised.setOnClickListener { viewModel.plus() }
        btnFlat.setOnClickListener { viewModel.minus() }
    }

    override fun onStart() {
        super.onStart()

        FirebaseAuth.getInstance().currentUser?.let {
            var str = "UID: ${it.uid}\n"
            str += "Name: ${it.displayName}\n"
            str += "Anonymous: ${it.isAnonymous}\n"

            for (provider in it.providerData) {
                str += "Provider: ${provider.providerId}\n"
            }

            tvFirebaseUser.text = str
        }
    }
}