package com.renderson.desafiopicpay.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.renderson.desafiopicpay.data.model.CreditCard

@Dao
interface CreditCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(creditCard: CreditCard?)

    @Query("SELECT * FROM credit_card")
    fun getAll(): List<CreditCard?>?

    @Query("DELETE FROM credit_card")
    fun delete()
}