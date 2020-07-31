package com.oneseven.codetest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oneseven.codetest.model.UserInfo

class UserInfoViewModel(val infoRepository: UserInfoRepository) : ViewModel() {

    val userInfos : MutableLiveData<MutableList<UserInfo>> = MutableLiveData()

    init {
        userInfos.value = ArrayList()
    }

    val taskFinished = object : OnTaskFinished {

        override fun onFinished(data: List<UserInfo>?) {
            if(data != null) {
                userInfos.value?.addAll(data)
            }
            userInfos.postValue(userInfos.value)
        }

    }

    fun getUserInfos() : LiveData<MutableList<UserInfo>> {
        return userInfos;
    }

    fun loadMoreUserInfos() {
        infoRepository.loadUserInfo(taskFinished);
    }

}