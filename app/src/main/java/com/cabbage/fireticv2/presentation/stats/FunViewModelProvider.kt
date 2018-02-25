package com.cabbage.fireticv2.presentation.stats

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.cabbage.fireticv2.data.FireTicRepository

class FunViewModelProvider(private val repository: FireTicRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FunViewModel(repository) as T
    }
}