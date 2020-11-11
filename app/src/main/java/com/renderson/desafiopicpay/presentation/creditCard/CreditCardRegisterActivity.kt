package com.renderson.desafiopicpay.presentation.creditCard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.presentation.ViewModelFactoryDB
import kotlinx.android.synthetic.main.activity_card_register.register_btn_card
import kotlinx.android.synthetic.main.activity_card_register.register_card_number
import kotlinx.android.synthetic.main.activity_card_register.register_cvv
import kotlinx.android.synthetic.main.activity_card_register.register_name
import kotlinx.android.synthetic.main.activity_card_register.register_expiration_date
import kotlinx.android.synthetic.main.activity_card_register.inputDate
import kotlinx.android.synthetic.main.activity_transaction.actionArrow
import java.util.Calendar

class CreditCardRegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: CreditCardRegisterViewModel

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

        register_btn_card.setOnClickListener {
            saveCreditCard()
        }

        viewModel = ViewModelProvider(
            viewModelStore,
            ViewModelFactoryDB(this)
        ).get(
            CreditCardRegisterViewModel::class.java
        )

        viewModel.getCreditCard()

        this.insertValues()
        this.iniFields()
    }

    private fun insertValues() {
        viewModel.getCardEvent.observe(this, Observer {
            it.map { items ->
                register_card_number.setText(items!!.card_number)
                register_name.setText(items.name)
                register_expiration_date.setText(items.expiry_date)
                register_cvv.setText(items.cvv)

                validateCardNumber(items.card_number)
                validateNameCard(items.name)
                validateFieldDate(items.expiry_date)
                validateCVV(items.cvv)
                showButton()
            }
        })
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

    private fun validateExpirationDate(s: String) {
        this.validateFieldDate(s)
    }

    private fun validateFieldDate(s: String) {
        val before = 0
        var working = s
        var isValid = true
        if (working.length == 2 && before == 0) {
            if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                isValid = false
            } else {
                working += "/"
                register_expiration_date.setText(working)
                register_expiration_date.setSelection(working.length)
            }
        }
        if (working.length == 5 && before == 0) {
            val enteredYear = working.substring(3)
            val currentYear = Calendar.getInstance()
                .get(Calendar.YEAR) % 100//getting last 2 digits of current year i.e. 2018 % 100 = 18
            if (Integer.parseInt(enteredYear) < currentYear && currentYear >= currentYear) {
                isValid = false
            }
        } else if (working.length != 5) {
            isValid = false
        }

        if (!isValid) {
            inputDate.error = "MM/YY"
            validateExpirationDate = false
        } else {
            inputDate.error = null
            validateExpirationDate = true
            this.showButton()
        }
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

    private fun saveCreditCard() {
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
        viewModel.saveCreditCard(cardNumber, name, expiration, cvv)
        finish()
    }

    override fun onResume() {
        super.onResume()
        this.iniFields()
    }
}