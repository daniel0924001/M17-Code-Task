package com.oneseven.codetest.model

import com.google.gson.annotations.SerializedName

class UserInfo {

    @SerializedName("login")
    var login: String? = null
    @SerializedName("avatar_url")
    var avatar_url: String? = null
    @SerializedName("url")
    var url: String? = null

}