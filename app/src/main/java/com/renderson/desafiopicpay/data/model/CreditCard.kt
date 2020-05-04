package com.renderson.desafiopicpay.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credit_card")
data class CreditCard(
    @PrimaryKey val card_number: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "expiry_date") val expiry_date: String,
    @ColumnInfo(name = "cvv") val cvv: String
)