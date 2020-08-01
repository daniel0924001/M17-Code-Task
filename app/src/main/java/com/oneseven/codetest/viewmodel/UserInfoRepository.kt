package com.oneseven.codetest.viewmodel

import androidx.annotation.NonNull
import com.oneseven.codetest.model.UserInfo
import com.oneseven.codetest.model.UserList
import com.oneseven.codetest.network.ApiService
import com.oneseven.codetest.network.AppClientManager
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Thread.sleep
import java.util.concurrent.Executors

class UserInfoRepository {

    val apiService = AppClientManager.client.create(ApiService::class.java)

    var pageNumber = 0

    fun loadUserInfo(input: String): Single<UserList> {
        pageNumber++
        return apiService.getUserInfos(input, pageNumber)
    }
}

interface OnTaskFinished {
    fun onFinished(data : List<UserInfo>?)
}