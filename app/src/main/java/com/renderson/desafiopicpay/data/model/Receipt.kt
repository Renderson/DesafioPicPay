package com.renderson.desafiopicpay.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Receipt(
    @SerializedName("id") val id: Int,
    @SerializedName("timestamp") val timestamp: Int,
    @SerializedName("value") val value: String,
    @SerializedName("destination_user") val destination_user: User,
    @SerializedName("success") val success: Boolean,
    @SerializedName("status") val status: String
) : Serializable