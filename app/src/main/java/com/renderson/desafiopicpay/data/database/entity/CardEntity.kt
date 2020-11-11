package com.renderson.desafiopicpay.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "credit_card")
data class CardEntity (
    @PrimaryKey val card_number: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "expiry_date") val expiry_date: String,
    @ColumnInfo(name = "cvv") val cvv: String
): Parcelable