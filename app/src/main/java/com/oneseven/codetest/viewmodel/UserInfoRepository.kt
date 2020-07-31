package com.oneseven.codetest.viewmodel

import android.util.Log
import androidx.annotation.NonNull
import com.oneseven.codetest.model.UserInfo
import com.oneseven.codetest.model.UserList
import com.oneseven.codetest.network.ApiService
import com.oneseven.codetest.network.AppClientManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Thread.sleep
import java.util.concurrent.Executors

class UserInfoRepository {

    var pageNumber = 1

    fun loadUserInfo(input: String, @NonNull task: OnTaskFinished) {
        val apiService = AppClientManager.client.create(ApiService::class.java)
        apiService.getUserInfos(input, pageNumber).enqueue(object : Callback<UserList> {
            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                response.body()?.items?.forEach {
                    Log.i("UserInfoRepository", "name: ${it.login}, page: $pageNumber")
                }
                pageNumber++
                task.onFinished(response.body()?.items)
            }

            override fun onFailure(call: Call<UserList>, t: Throwable) {
                Log.e("UserInfoRepository", "onFailure ${t.toString()}")
            }
        })
    }
}

interface OnTaskFinished {
    fun onFinished(data : List<UserInfo>?)
}