package com.oneseven.codetest.model

import com.google.gson.annotations.SerializedName

class UserList {
    @SerializedName("items")
    val items: List<UserInfo>? = null
}