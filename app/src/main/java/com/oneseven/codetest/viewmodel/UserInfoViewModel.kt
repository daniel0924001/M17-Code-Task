package com.oneseven.codetest.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oneseven.codetest.model.UserDetail
import com.oneseven.codetest.model.UserInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserInfoViewModel(private val infoRepository: UserInfoRepository) : ViewModel() {

    private val userInfos : MutableLiveData<MutableList<UserInfo>> = MutableLiveData()
    private val recentThrowable: MutableLiveData<Throwable> = MutableLiveData()
    private val loading: MutableLiveData<Boolean> = MutableLiveData()
    private val userDetail : MutableLiveData<UserDetail> = MutableLiveData()

    init {
        userInfos.value = ArrayList()
        loading.value = false
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

    fun getUserDetail() : LiveData<UserDetail> {
        return userDetail
    }

    @SuppressLint("CheckResult")
    fun loadMoreUserInfos(input : String) {

        infoRepository.loadUserInfo(input)
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

    @SuppressLint("CheckResult")
    fun loadUserDetail(userName : String?) {

        if(userName == null) {
            return
        }

        infoRepository.loadUserDetail(userName)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                userDetail.value = it
            },{
                Log.e("UserInfoViewModel", it.toString())
                recentThrowable.value = it
            })
    }

}