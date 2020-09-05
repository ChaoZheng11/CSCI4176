package com.chen.vax.dashplay.ui.Setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareVm : ViewModel() {

    private val a = MutableLiveData<String>().apply {
        value = ""
    }
    val x: LiveData<String> = a

    private val b = MutableLiveData<String>().apply {
        value = ""
    }
    val y: LiveData<String> = b


    private val c = MutableLiveData<String>().apply {
        value = ""
    }
    val z: LiveData<String> = c
}