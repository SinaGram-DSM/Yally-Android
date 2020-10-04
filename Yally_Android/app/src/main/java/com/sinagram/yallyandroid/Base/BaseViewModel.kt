package com.sinagram.yallyandroid.Base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
}