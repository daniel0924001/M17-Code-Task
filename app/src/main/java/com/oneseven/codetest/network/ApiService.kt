package com.oneseven.codetest.network

import com.oneseven.codetest.model.UserList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun getUserInfos(@Query("q") name : String, @Query("page") page : Int): Single<UserList>

}
