package com.oneseven.codetest.model

import com.google.gson.annotations.SerializedName

class UserDetail {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("avatar_url")
    var avatarUrl: String? = null
    @SerializedName("bio")
    var bio: String? = null
}