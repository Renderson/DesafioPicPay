package com.renderson.desafiopicpay.presentation.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.renderson.desafiopicpay.data.model.Receipt
import com.renderson.desafiopicpay.data.model.Transaction
import com.renderson.desafiopicpay.data.network.ServiceCallBack
import com.renderson.desafiopicpay.data.network.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionViewModel(private val repository: Repository) : ViewModel() {

    private val _transactionLiveData: MutableLiveData<Receipt> = MutableLiveData()
    val transactionLiveData: MutableLiveData<Receipt> = _transactionLiveData

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message

    fun transaction(transaction: Transaction) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    repository.transaction(transaction) { result ->
                        when (result) {
                            is ServiceCallBack.SuccessTransaction -> {
                                _transactionLiveData.value = result.transaction
                            }
                            is ServiceCallBack.ApiError -> {
                                if (result.statusCode == 401) {
                                    _message.value = "Error API"
                                } else {
                                    _message.value = "Error não tratado"
                                }
                            }
                            is ServiceCallBack.ServerError -> {
                                _message.value = "Error Server"
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }
}