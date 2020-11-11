package com.renderson.desafiopicpay.presentation.transaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.data.database.entity.CardEntity
import com.renderson.desafiopicpay.data.model.Transaction
import com.renderson.desafiopicpay.data.model.User
import com.renderson.desafiopicpay.data.network.ApiService
import com.renderson.desafiopicpay.data.network.repository.ServiceApiDataSource
import com.renderson.desafiopicpay.presentation.ViewModelFactory
import com.renderson.desafiopicpay.presentation.ViewModelFactoryDB
import com.renderson.desafiopicpay.presentation.creditCard.CreditCardHomeActivity
import com.renderson.desafiopicpay.presentation.creditCard.CreditCardRegisterActivity
import com.renderson.desafiopicpay.presentation.creditCard.CreditCardRegisterViewModel
import com.renderson.desafiopicpay.presentation.receipt.ReceiptFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_transaction.actionArrow
import kotlinx.android.synthetic.main.activity_transaction.transaction_edit
import kotlinx.android.synthetic.main.activity_transaction.payment_user_image
import kotlinx.android.synthetic.main.activity_transaction.payment_username
import kotlinx.android.synthetic.main.activity_transaction.transaction_card
import kotlinx.android.synthetic.main.activity_transaction.transaction_btn_payment
import kotlinx.android.synthetic.main.activity_transaction.transaction_value

open class TransactionActivity : TransactionBasic() {

    private lateinit var viewModelDb: CreditCardRegisterViewModel
    private lateinit var cardEntity: List<CardEntity?>

    private var cardNumberDao: String? = "-----------------"
    private var expiryDateDao: String? = null
    private var cvvDao: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        val viewModel = ViewModelProvider(
            viewModelStore,
            ViewModelFactory(ServiceApiDataSource(ApiService.serviceInterface))
        ).get(TransactionViewModel::class.java)

        viewModelDb = ViewModelProvider(
            viewModelStore,
            ViewModelFactoryDB(this)
        ).get(CreditCardRegisterViewModel::class.java)

        val user = intent.getSerializableExtra(USER_MODEL) as User

        initSettings()
        this.populateView(user)

        actionArrow.setOnClickListener {
            finish()
        }

        transaction_edit.setOnClickListener {
            if (cardEntity.isEmpty()) {
                val intent = Intent(this, CreditCardHomeActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, CreditCardRegisterActivity::class.java)
                startActivity(intent)
            }
        }

        this.getInfoCardDataBase()

        this.sendTransaction(user, viewModel)

        this.getInfoTransaction(viewModel)
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

    private fun getInfoCardDataBase() {
        viewModelDb.getCardEvent.observe(this, Observer {
            cardEntity = it
            if (it!!.isEmpty()) {
                Toast.makeText(this, getString(R.string.txt_sign_credit_card), Toast.LENGTH_LONG)
                    .show()
            } else {
                it.map { card ->
                    cardNumberDao = card!!.card_number
                    expiryDateDao = card.expiry_date
                    cvvDao = card.cvv
                }
                this.setInfoCardNumber()
            }
        })
    }

    private fun setInfoCardNumber() {
        val cardNumberEdit = cardNumberDao!!.substring(cardNumberDao!!.length - 4)
        transaction_card.text = (resources.getString(R.string.txt_flag_card, cardNumberEdit))
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

    private fun getInfoTransaction(viewModel: TransactionViewModel) {
        viewModel.transactionLiveData.observe(this, Observer {
            it.let { transaction ->
                if (transaction.status == getString(R.string.txt_transaction_approved)) {

                    val bundle = Bundle()
                    bundle.putSerializable(TRANSACTION_DETAIL, transaction)
                    bundle.putString(CARD_NUMBER, cardNumberDao)
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
                transactionValuesListener(s.toString(), cardEntity)
            }
        })
    }

    companion object {
        private const val USER_MODEL = "user_model"
        private const val TRANSACTION_DETAIL = "transaction_detail"
        private const val CARD_NUMBER = "cardNumber"

        fun getStartIntent(context: Context, user: User): Intent {
            return Intent(context, TransactionActivity::class.java).apply {
                putExtra(USER_MODEL, user)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()
        viewModelDb.getCreditCard()
        this.listenerTransactionValue()
        this.setInfoCardNumber()
    }
}