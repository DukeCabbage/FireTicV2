package com.cabbage.fireticv2

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @OnClick(R.id.btn_menu_about)
    fun onClick() {
        val packageInfo = packageManager.getPackageInfo(this.packageName, 0)
        val strVersion = "${packageInfo.versionName} (${packageInfo.versionCode})"
        Snackbar.make(rootView, strVersion, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
    }
}
