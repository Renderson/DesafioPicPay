package com.renderson.desafiopicpay.presentation.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun dateFormat(): Pair<String, String> {
    val date = Date()
    val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    val hour = Date()
    val formatterHora: DateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

    val dateFormat = formatter.format(date).toString()
    val hourFormat = formatterHora.format(hour)
    return Pair(dateFormat, hourFormat)
}