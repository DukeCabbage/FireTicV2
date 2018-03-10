package com.cabbage.fireticv2.presentation.home

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.cabbage.fireticv2.data.FireTicRepository

class MainVMFactory(private val repository: FireTicRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}