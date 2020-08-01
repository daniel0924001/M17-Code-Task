package com.oneseven.codetest.network

import com.oneseven.codetest.model.UserDetail
import com.oneseven.codetest.model.UserList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun getUserInfos(@Query("q") name : String, @Query("page") page : Int): Single<UserList>

    @GET("users/{user_name}")
    fun getUserDetail(@Path("user_name") user_name : String): Single<UserDetail>

}
