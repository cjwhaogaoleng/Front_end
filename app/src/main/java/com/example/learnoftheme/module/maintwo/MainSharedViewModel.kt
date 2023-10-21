package com.example.learnoftheme.module.maintwo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainSharedViewModel : ViewModel() {


    var input:MutableLiveData<String> = MutableLiveData<String>()

    var history: MutableLiveData<List<String>> = MutableLiveData()

    var result: MutableLiveData<String> = MutableLiveData()



}