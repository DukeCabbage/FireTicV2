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
    private var d2: ModelUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        ButterKnife.bind(this)
        setUpActionBar(toolbar)

        activityComponent.inject(this)

        mViewModel.firebaseUser().observe(this, Observer {
            d1 = it

            var str = "UID: ${d1?.uid}\n"
            str += "Name: ${d1?.displayName}\n"
            str += "Anonymous: ${d1?.isAnonymous}\n"

            for (provider in d1?.providerData ?: emptyList()) {
                str += "Provider: ${provider.providerId}\n"
            }
            tvFirebaseUser.text = str
        })

        mViewModel.signedInUser().observe(this, Observer {
            d2 = it

            tvUserName.text = d2?.name
            etUserName.setText(d2?.name)
            etUserName.setSelection(d2?.name?.length ?: 0)
        })

        btnRaised.setOnClickListener {
            val oldName = d2?.name
            val newName = etUserName.text.toString()

            if (newName == oldName || newName.isBlank()) return@setOnClickListener

            mViewModel.updateUserName(newName)
        }
    }
}