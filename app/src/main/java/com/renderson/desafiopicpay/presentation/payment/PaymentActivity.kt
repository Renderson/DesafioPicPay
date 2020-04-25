package com.renderson.desafiopicpay.presentation.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.data.model.Transaction
import com.renderson.desafiopicpay.data.model.User
import com.renderson.desafiopicpay.presentation.ContactsViewModel
import com.renderson.desafiopicpay.presentation.receipt.ReceiptFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_payment.*

open class PaymentActivity : PaymentBasic() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val viewModel: ContactsViewModel = ViewModelProviders.of(this).get(
            ContactsViewModel::class.java
        )

        val user = intent.getSerializableExtra(USER_MODEL) as User

        initSettings()
        this.populateView(user)
        this.listenerTransactionValue()

        actionArrow.setOnClickListener {
            finish()
        }

        this.sendTransaction(user, viewModel)

        this.getInfoTransaction(viewModel)
    }

    private fun getInfoTransaction(viewModel: ContactsViewModel) {
        viewModel.transactionLiveData.observe(this, Observer {
            it.let { transaction ->
                if (transaction.status == "Aprovada") {

                    val bundle = Bundle()
                    bundle.putSerializable("transaction_detail", transaction)
                    val fragment = ReceiptFragment()
                    fragment.arguments = bundle
                    fragment.show(supportFragmentManager, fragment.tag)

                    //finish()
                } else {
                    Toast.makeText(this, "Transação: ${transaction.status}", Toast.LENGTH_LONG)
                        .show()
                }
                //return@Observer
            }
        })
    }

    private fun sendTransaction(
        user: User,
        viewModel: ContactsViewModel
    ) {
        transaction_btn_payment.setOnClickListener {
            val valor: String = transaction_value.text.toString()
            val transaction = Transaction(
                card_number = "1111111111111111",
                cvv = 789,
                value = valor,
                expiry_date = "01/18",
                destination_user_id = user.id
            )
            viewModel.transactionEntryUser(transaction)

            /*val date = Date()
            val formatter: DateFormat = SimpleDateFormat("dd/MMM/yyyy HH:mm")
            Log.i("DATE", formatter.format(date))*/
        }
    }

    private fun populateView(user: User) {
        user.let { user ->
            Picasso.get()
                .load(user.img)
                .placeholder(R.drawable.ic_account_default)
                .into(payment_user_image)

            payment_username.text = user.username
        }
    }

    private fun listenerTransactionValue() {
        transaction_value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                transaction_value.removeTextChangedListener(this)
                transaction_value.addTextChangedListener(this)
                transactionValuesListener(s.toString())
            }
        })
    }

    companion object {
        private const val USER_MODEL = "user_model"

        fun getStartIntent(context: Context, user: User): Intent {
            return Intent(context, PaymentActivity::class.java).apply {
                putExtra(USER_MODEL, user)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}