package com.renderson.desafiopicpay.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.renderson.desafiopicpay.data.database.entity.CardEntity

@Dao
interface CreditCardDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(creditCard: CardEntity)

    @Query("SELECT * FROM credit_card")
    suspend fun getCard(): List<CardEntity?>?

    @Query("DELETE FROM credit_card")
    suspend fun delete()
}