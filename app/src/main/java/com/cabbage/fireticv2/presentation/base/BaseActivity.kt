package com.cabbage.fireticv2.presentation.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.cabbage.fireticv2.MyApplication
import com.cabbage.fireticv2.dagger.activity.ActivityComponent
import com.cabbage.fireticv2.dagger.activity.ActivityModule
import com.cabbage.fireticv2.dagger.activity.ConfigPersistComponent
import com.cabbage.fireticv2.dagger.activity.DaggerConfigPersistComponent
import com.cabbage.fireticv2.presentation.utils.ViewUtil
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private const val KEY_ACTIVITY_ID = "ACTIVITY_ID"
        private val NextId = AtomicLong(0)
        private val ComponentMap = HashMap<Long, ConfigPersistComponent>()
    }

    lateinit var activityComponent: ActivityComponent
    private var mActivityId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the ActivityComponent and reuses cached ConfigPersistComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState?.getLong(KEY_ACTIVITY_ID) ?: NextId.getAndIncrement()
        mActivityId!!.let {
            val myApp = MyApplication.getInstance(this)
            val configPersistentComponent = ComponentMap[it]
                                            ?: DaggerConfigPersistComponent.builder()
                                                    .appComponent(myApp.appComponent)
                                                    .build()

            if (!ComponentMap.containsKey(it)) {
                ComponentMap[it] = configPersistentComponent
            }

            activityComponent = configPersistentComponent.activityComponent(ActivityModule(this))
        }
    }

    @CallSuper
    protected open fun setUpActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Compensate for translucent status bar
        ViewUtil.getStatusBarHeight(this)?.let { statusBarHeight ->
            toolbar.setPadding(
                    toolbar.paddingLeft,
                    toolbar.paddingTop + statusBarHeight,
                    toolbar.paddingRight,
                    toolbar.paddingBottom)

            toolbar.layoutParams.height += statusBarHeight
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.v("Saving activity_home id=$mActivityId")
        outState.putLong(KEY_ACTIVITY_ID, mActivityId!!)
    }

    override fun onDestroy() {
        if (!isChangingConfigurations) {
            Timber.i("Clearing ConfigPersistComponent id=$mActivityId")
            ComponentMap.remove(mActivityId)
        }
        super.onDestroy()
    }
}
