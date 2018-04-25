package com.cabbage.fireticv2.presentation.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.cabbage.fireticv2.dagger.ApplicationScope
import com.cabbage.fireticv2.data.FireTicRepository
import timber.log.Timber
import javax.inject.Inject


@ApplicationScope
class VMFactory
@Inject constructor(private val repository: FireTicRepository)
    : ViewModelProvider.NewInstanceFactory() {

    init {
        Timber.v("Init")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        Timber.v("create: ${modelClass.simpleName}")
        // TODO: Experimental...
        return try {
            val constructor = modelClass.getDeclaredConstructor(FireTicRepository::class.java)
            constructor.newInstance(repository)
        } catch (e: Exception) {
            Timber.e(e)
            FallbackVM() as T
        }
    }

    private class FallbackVM : ViewModel()
}