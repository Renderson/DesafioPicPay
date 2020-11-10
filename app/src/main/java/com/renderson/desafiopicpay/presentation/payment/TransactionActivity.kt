package com.renderson.desafiopicpay.presentation.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.data.database.DataBaseClient
import com.renderson.desafiopicpay.data.model.CreditCard
import com.renderson.desafiopicpay.data.model.Transaction
import com.renderson.desafiopicpay.data.model.User
import com.renderson.desafiopicpay.data.network.ApiService
import com.renderson.desafiopicpay.data.network.repository.ServiceApiDataSource
import com.renderson.desafiopicpay.presentation.ViewModelFactory
import com.renderson.desafiopicpay.presentation.creditCard.PrimingCardActivity
import com.renderson.desafiopicpay.presentation.receipt.ReceiptFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_transaction.*

open class TransactionActivity : TransactionBasic() {

    private var list: List<CreditCard?>? = null
    private var cardNumberDao: String? = "-----------------"
    private var expiryDateDao: String? = null
    private var cvvDao: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        val viewModel = ViewModelProvider(viewModelStore,
            ViewModelFactory(ServiceApiDataSource(ApiService.serviceInterface))
        ).get(
            TransactionViewModel::class.java)

        //this.getInfoCardDataBase()

        val user = intent.getSerializableExtra(USER_MODEL) as User

        initSettings()
        this.populateView(user)
        this.listenerTransactionValue()

        actionArrow.setOnClickListener {
            finish()
        }

        transaction_edit.setOnClickListener {
            val intent = Intent(this, PrimingCardActivity::class.java)
            startActivity(intent)
        }

        this.sendTransaction(user, viewModel)

        this.getInfoTransaction(viewModel)
    }

    private fun setInfoCardNumber() {
        val cardNumberEdit = cardNumberDao!!.substring(cardNumberDao!!.length - 4)
        transaction_card.text = (resources.getString(R.string.txt_flag_card, cardNumberEdit))
    }

    private fun getInfoCardDataBase() {
        list =
            DataBaseClient.getInstance(applicationContext)?.appDatabase?.creditCardDao()?.getAll()!!

        if (list!!.isEmpty()) {
            val intent = Intent(this, PrimingCardActivity::class.java)
            startActivity(intent)
            finish()
        } else {

            list!!.map {
                cardNumberDao = it!!.card_number
                expiryDateDao = it.expiry_date
                cvvDao = it.cvv
            }
            this.setInfoCardNumber()
        }
    }

    private fun getInfoTransaction(viewModel: TransactionViewModel) {
        viewModel.transactionLiveData.observe(this, Observer {
            it.let { transaction ->
                if (transaction.status == "Aprovada") {

                    val bundle = Bundle()
                    bundle.putSerializable("transaction_detail", transaction)
                    val fragment = ReceiptFragment()
                    fragment.arguments = bundle
                    fragment.show(supportFragmentManager, fragment.tag)

                    transaction_value.setText(getString(R.string.txt_value))

                } else {
                    viewModel.message.observe(this, Observer { message ->
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    })
                }
            }
        })
    }

    private fun sendTransaction(
        user: User,
        viewModel: TransactionViewModel
    ) {
        transaction_btn_payment.setOnClickListener {
            val valor: String = transaction_value.text.toString()
            val transaction = Transaction(
                card_number = cardNumberDao.toString(),
                cvv = cvvDao.toString(),
                value = valor,
                expiry_date = expiryDateDao.toString(),
                destination_user_id = user.id
            )
            viewModel.transaction(transaction)
        }
    }

    private fun populateView(user: User) {
        user.let {
            Picasso.get()
                .load(it.img)
                .placeholder(R.drawable.ic_account_default)
                .into(payment_user_image)

            payment_username.text = it.username
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
            return Intent(context, TransactionActivity::class.java).apply {
                putExtra(USER_MODEL, user)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        DataBaseClient.getInstance(this)?.destroyInstance()
        finish()
    }

    override fun onResume() {
        super.onResume()
        this.getInfoCardDataBase()
        this.setInfoCardNumber()
    }

    override fun onDestroy() {
        super.onDestroy()
        DataBaseClient.getInstance(this)?.destroyInstance()
    }
}