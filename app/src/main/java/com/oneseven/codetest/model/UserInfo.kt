package com.oneseven.codetest.model

import com.google.gson.annotations.SerializedName

class UserInfo {

    @SerializedName("login")
    var login: String? = null
    @SerializedName("avatar_url")
    var avatarUrl: String? = null

    var itemType: Int = -1

}