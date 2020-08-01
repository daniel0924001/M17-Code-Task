package com.oneseven.codetest.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oneseven.codetest.model.UserInfo
import com.oneseven.codetest.model.UserList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.function.Function

class UserInfoViewModel(val infoRepository: UserInfoRepository) : ViewModel() {

    val userInfos : MutableLiveData<MutableList<UserInfo>> = MutableLiveData()
    val recentThrowable: MutableLiveData<Throwable> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        userInfos.value = ArrayList()
        loading.value = false
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
        return userInfos
    }

    fun getRecentThrowable() : LiveData<Throwable> {
        return recentThrowable
    }

    fun getUILoading() : MutableLiveData<Boolean> {
        return loading
    }

    @SuppressLint("CheckResult")
    fun loadMoreUserInfos(input : String) {

        infoRepository.loadUserInfo(input, taskFinished)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.items != null) {
                    userInfos.value?.addAll(it.items.asIterable())
                }
                userInfos.value = userInfos.value
            }, {
                Log.e("UserInfoViewModel", it.toString())
                recentThrowable.value = it
            })
    }

}