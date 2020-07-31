package com.oneseven.codetest.network

import com.oneseven.codetest.model.UserInfo
import com.oneseven.codetest.model.UserList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun getUserInfos(@Query("q") name : String, @Query("page") page : Int): Call<UserList>

}
