package com.renderson.desafiopicpay.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.renderson.desafiopicpay.data.model.CreditCard

@Database(entities = [CreditCard::class], version = 2, exportSchema = false)
abstract class ManagerDao : RoomDatabase() {
    abstract fun creditCardDao(): CreditCardDao?
}