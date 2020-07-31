package com.oneseven.codetest.model

import com.google.gson.annotations.SerializedName

class UserList {
    @SerializedName("items")
    var items: List<UserInfo>? = null
}