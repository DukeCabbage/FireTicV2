package com.cabbage.fireticv2.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

class FireTicRepository {

    private val data = MutableLiveData<Int>()

    private var initialized = false

    fun getGameData(): LiveData<Int> {
        if (!initialized) {
            initialized = true
            data.value = 0
        }
        return data
    }

    fun increase() {
        data.value = if (data.value == null) 0
        else data.value!! + 1
    }

    fun decrease() {
        data.value = if (data.value == null) 0
        else data.value!! - 1
    }
}