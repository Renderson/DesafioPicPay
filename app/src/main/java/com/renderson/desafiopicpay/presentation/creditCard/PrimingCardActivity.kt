package com.renderson.desafiopicpay.presentation.creditCard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.data.model.CreditCard
import kotlinx.android.synthetic.main.activity_payment.actionArrow
import kotlinx.android.synthetic.main.activity_priming_card.*

class PrimingCardActivity: AppCompatActivity() {

    private var list: CreditCard? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_priming_card)

        actionArrow.setOnClickListener {
            finish()
        }

        btn_sign_card.setOnClickListener{
            val intent = Intent(this, CardRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}