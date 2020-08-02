package com.oneseven.codetest.viewmodel

import com.oneseven.codetest.model.UserDetail
import com.oneseven.codetest.model.UserList
import com.oneseven.codetest.network.ApiService
import com.oneseven.codetest.network.AppClientManager
import io.reactivex.Single

class UserInfoRepository {

    private val apiService = AppClientManager.client.create(ApiService::class.java)

    private var pageNumber = 0

    fun loadUserInfo(input: String): Single<UserList> {
        pageNumber++
        return apiService.getUserInfos(input, pageNumber)
    }

    fun loadUserDetail(userName: String): Single<UserDetail> {
        return apiService.getUserDetail(userName)
    }
}