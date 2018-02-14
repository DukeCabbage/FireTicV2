package com.cabbage.fireticv2.presentation.stats

import android.os.Bundle
import butterknife.ButterKnife
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