package com.chen.vax.dashplay.ui.Main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val x = MutableLiveData<String>().apply {
        value = "Welcome to DashPlay"
    }
    val text: LiveData<String> = x



    private val y= MutableLiveData<String>().apply {
        value = "History"
    }
    val write: LiveData<String> = y


}