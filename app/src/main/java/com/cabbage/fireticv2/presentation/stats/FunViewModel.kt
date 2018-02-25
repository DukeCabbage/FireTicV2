package com.cabbage.fireticv2.presentation.stats

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.cabbage.fireticv2.data.FireTicRepository

class FunViewModel(private val repository: FireTicRepository) : ViewModel() {

    fun getData(): LiveData<Int> {
        return repository.getGameData()
    }

    fun plus() {
        repository.increase()
    }

    fun minus() {
        repository.decrease()
    }
}