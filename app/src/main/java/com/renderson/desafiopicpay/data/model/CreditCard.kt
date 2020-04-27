package com.renderson.desafiopicpay.data.model

data class CreditCard(
    val card_number: String,
    val name: String,
    val expiry_date: String,
    val cvv: String
)