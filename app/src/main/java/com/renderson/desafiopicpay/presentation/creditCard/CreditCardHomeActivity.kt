package com.renderson.desafiopicpay.presentation.creditCard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.renderson.desafiopicpay.R
import kotlinx.android.synthetic.main.activity_priming_card.btn_sign_card
import kotlinx.android.synthetic.main.activity_transaction.actionArrow

class CreditCardHomeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_priming_card)

        actionArrow.setOnClickListener {
            finish()
        }

        btn_sign_card.setOnClickListener{
            val intent = Intent(this, CreditCardRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}