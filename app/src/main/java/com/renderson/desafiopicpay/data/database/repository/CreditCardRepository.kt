package com.renderson.desafiopicpay.data.database.repository

import com.renderson.desafiopicpay.data.database.entity.CardEntity

interface CreditCardRepository {

    suspend fun save(cardNumber: String, name: String, expiration: String, cvv: String)

    suspend fun getCard(): List<CardEntity?>?

    suspend fun delete()
}