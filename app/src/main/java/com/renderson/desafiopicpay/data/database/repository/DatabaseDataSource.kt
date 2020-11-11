package com.renderson.desafiopicpay.data.database.repository

import com.renderson.desafiopicpay.data.database.dao.CreditCardDAO
import com.renderson.desafiopicpay.data.database.entity.CardEntity

class DatabaseDataSource(private val creditCardDAO: CreditCardDAO): CreditCardRepository {

    override suspend fun save(cardNumber: String, name: String, expiration: String, cvv: String) {
        val cardEntity = CardEntity(
            card_number = cardNumber,
            name = name,
            expiry_date = expiration,
            cvv = cvv
        )
        return creditCardDAO.save(cardEntity)
    }

    override suspend fun getCard(): List<CardEntity?>? {
        return creditCardDAO.getCard()
    }

    override suspend fun delete() {
        creditCardDAO.delete()
    }
}