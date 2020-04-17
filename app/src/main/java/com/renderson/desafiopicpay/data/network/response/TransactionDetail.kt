package com.renderson.desafiopicpay.data.network.response

import com.renderson.desafiopicpay.data.model.User

data class TransactionDetail (
    val id: Int,
    val timestamp: Int,
    val value: String,
    val destination_user: User?,
    val success: Boolean,
    val status: String
)