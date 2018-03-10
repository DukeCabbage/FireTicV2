package com.cabbage.fireticv2.presentation.stats

import android.arch.lifecycle.Observer
import android.os.Bundle
import butterknife.ButterKnife
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.data.user.ModelUser
import com.cabbage.fireticv2.presentation.base.BaseActivity
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_stats.*
import kotlinx.android.synthetic.main.include_appbar_basic.*
import javax.inject.Inject

class StatsActivity : BaseActivity() {

    @Inject lateinit var mViewModel: UserAccountViewModel

    private var d1: FirebaseUser? = null
        set(value) {
            field = value
            refresh()
        }
    private var d2: ModelUser? = null
        set(value) {
            field = value
            refresh()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        ButterKnife.bind(this)
        setUpActionBar(toolbar)

        activityComponent.inject(this)

        mViewModel.firebaseUser().observe(this, Observer {
            d1 = it
        })

        mViewModel.signedInUser().observe(this, Observer {
            d2 = it
        })
    }

    private fun refresh() {
        if (d1 == null || d2 == null) return

        var str = "UID: ${d1?.uid}\n"
        str += "Name: ${d2?.name}\n"
        str += "Anonymous: ${d1?.isAnonymous}\n"

        for (provider in d1?.providerData ?: emptyList()) {
            str += "Provider: ${provider.providerId}\n"
        }

        tvFirebaseUser.text = str
    }
}