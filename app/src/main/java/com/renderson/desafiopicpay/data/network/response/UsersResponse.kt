package com.renderson.desafiopicpay.data.network.response

import com.google.gson.annotations.SerializedName

data class UsersResponse (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("img") val img: String,
    @SerializedName("username") val username: String
)