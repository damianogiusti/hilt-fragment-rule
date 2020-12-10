package com.damianogiusti.hilt.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

abstract class HomeViewModel : ViewModel() {
    abstract val message: LiveData<String>
}

class DefaultHomeViewModel @Inject constructor() : HomeViewModel() {
    override val message: LiveData<String> = MutableLiveData("Welcome home!")
}