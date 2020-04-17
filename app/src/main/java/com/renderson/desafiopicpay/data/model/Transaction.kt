package com.renderson.desafiopicpay.data.model

data class Transaction(
    val card_number: String,
    val cvv: Int,
    val value: String,
    val expiry_date: String,
    val destination_user_id: Int
)