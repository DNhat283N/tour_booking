package com.project17.tourbooking.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel(): ViewModel(){
    val isChosenCategory = MutableLiveData<Boolean>()
    val priceRange = MutableLiveData<ClosedFloatingPointRange<Float>>()
    val rateMoreThan = MutableLiveData<Int>()
}