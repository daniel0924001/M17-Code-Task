package com.oneseven.codetest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserInfoFactory(private var infoRepository: UserInfoRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            return UserInfoViewModel(infoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}