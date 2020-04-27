package com.renderson.desafiopicpay.presentation.creditCard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.data.model.CreditCard
import kotlinx.android.synthetic.main.activity_card_register.*
import kotlinx.android.synthetic.main.activity_payment.actionArrow

class CardRegisterActivity : AppCompatActivity() {

    private var validateCardNumber = false
    private var validateNameCard = false
    private var validateExpirationDate = false
    private var validateCardCvv = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_register)

        actionArrow.setOnClickListener {
            finish()
        }

        this.iniFields()
    }

    private fun iniFields() {
        register_card_number.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                validateCardNumber(s.toString())
            }
        })

        register_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateNameCard(s.toString())
            }

        })

        register_expiration_date.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateExpirationDate(s.toString())
            }

        })

        register_cvv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateCVV(s.toString())
            }

        })
    }

    private fun validateCardNumber(value: String) {
        validateCardNumber = value.length == 16
        this.showButton()
    }

    private fun validateNameCard(value: String) {
        validateNameCard != value.isEmpty()
        this.showButton()
    }

    private fun validateExpirationDate(value: String) {
        validateExpirationDate = value.length == 5
        this.showButton()
    }

    private fun validateCVV(value: String) {
        validateCardCvv = value.length == 3
        this.showButton()
    }

    private fun showButton() {
        if (validateCardNumber && !validateNameCard && validateExpirationDate && validateCardCvv) {
            register_btn_card.visibility = View.VISIBLE
        } else {
            register_btn_card.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun saveCreditCard(view: View) {
        this.saveCreditCardDB(
            register_card_number.text.toString(),
            register_name.text.toString(),
            register_expiration_date.text.toString(),
            register_cvv.text.toString()
        )
    }

    private fun saveCreditCardDB(
        cardNumber: String,
        name: String,
        expiration: String,
        cvv: String
    ) {
        val creditCard = CreditCard(
            card_number = cardNumber,
            name = name,
            expiry_date = expiration,
            cvv = cvv
        )
        //model.saveCreditCard(creditCard)
        Log.i("SAVECARD", creditCard.toString())
    }
}