package com.renderson.desafiopicpay.presentation.payment

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.renderson.desafiopicpay.R
import kotlinx.android.synthetic.main.activity_payment.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.util.*
import kotlin.math.pow

private var value: String? = null
private var groupDivider: Char? = null
private var monetaryDivider: Char? = null
private var fractionDigit = 0
private var currencySymbol: String? = null
private var locale: Locale? = null
private var numberFormat: DecimalFormat? = null

open class PaymentBasic : AppCompatActivity() {

    fun transactionValuesListener(s: String) {
        var text = s
        try {
            if (text != value) {
                text = text.replace(groupDivider!!, '\u0020').replace(monetaryDivider!!, '\u0020')
                    .replace(".", "").replace(" ", "")
                    .replace(currencySymbol!!, "").trim { it <= ' ' }
                value = format(text, false)
                if (value != "0,00") setTransactionValue(
                    value!!,
                    ContextCompat.getColor(this, R.color.colorAccent),
                    true
                ) else setTransactionValue(
                    value!!,
                    ContextCompat.getColor(this, R.color.color_white),
                    false
                )
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun setTransactionValue(
        value: String,
        color: Int,
        enableButton: Boolean
    ) {
        transaction_value.setText(value)
        transaction_value.setSelection(value.length)
        transaction_currency.setTextColor(color)
        transaction_value.setTextColor(color)
        transaction_btn_payment.isEnabled = enableButton
    }

    fun initSettings() {
        locale = getDefaultLocale()
        val symbols =
            DecimalFormatSymbols.getInstance(locale)
        fractionDigit =
            Currency.getInstance(getDefaultLocale()).defaultFractionDigits
        groupDivider = symbols.groupingSeparator
        monetaryDivider = symbols.monetaryDecimalSeparator
        currencySymbol = symbols.currencySymbol
        numberFormat = DecimalFormat("#,###,##0.00", symbols)
    }

    private fun getDefaultLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) this.resources
            .configuration.locales.get(0) else this.resources
            .configuration.locale
    }

    @Throws(ParseException::class)
    private fun format(text: String, showSymbol: Boolean): String? {
        return if (showSymbol) numberFormat!!.format(
            text.toDouble() / 10.0.pow(fractionDigit.toDouble())
        ) else numberFormat!!.format(
            text.toDouble() / 10.0.pow(fractionDigit.toDouble())
        ).replace(currencySymbol!!, "")
    }
}