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

    fun loadUserInfo(@NonNull task: OnTaskFinished) {
        val apiService = AppClientManager.client.create(ApiService::class.java)
        apiService.getUserInfos("Daniel", pageNumber).enqueue(object : Callback<UserList> {
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

    fun loadUserInfoMock(@NonNull task: OnTaskFinished) {
        Executors.newSingleThreadExecutor().submit {
            val data = ArrayList<UserInfo>()

            Log.d("UserInfoRepository", "start")

            for (i in 1..10) {
                val userInfo = UserInfo()
                userInfo.login = "Daniel$i"
                userInfo.avatar_url = "https://static.zooniverse.org/www.zooniverse.org/assets/simple-avatar.png"
                userInfo.url = "https://api.github.com/users/Daniel15"
                data.add(userInfo)
            }

            Log.d("UserInfoRepository", "sleep start")
            sleep(3000);

            Log.d("UserInfoRepository", "task finished")
            task.onFinished(data)
        }
    }
}

interface OnTaskFinished {
    fun onFinished(data : List<UserInfo>?);
}