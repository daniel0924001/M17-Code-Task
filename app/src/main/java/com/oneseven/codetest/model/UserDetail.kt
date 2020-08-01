package com.oneseven.codetest.model

import com.google.gson.annotations.SerializedName

class UserDetail {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("avatar_url")
    var avatar_url: String? = null
    @SerializedName("bio")
    var bio: String? = null
}