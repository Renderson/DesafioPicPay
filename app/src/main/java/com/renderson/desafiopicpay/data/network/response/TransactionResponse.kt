package com.renderson.desafiopicpay.data.network.response

import com.renderson.desafiopicpay.data.model.Receipt

data class TransactionResponse(
    val transaction: Receipt?
)