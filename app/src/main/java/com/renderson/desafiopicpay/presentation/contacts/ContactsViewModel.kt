package com.renderson.desafiopicpay.presentation.contacts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.renderson.desafiopicpay.data.model.Receipt
import com.renderson.desafiopicpay.data.model.Transaction
import com.renderson.desafiopicpay.data.model.User
import com.renderson.desafiopicpay.data.network.ApiService
import com.renderson.desafiopicpay.data.network.ServiceCallBack
import com.renderson.desafiopicpay.data.network.repository.Repository
import com.renderson.desafiopicpay.data.network.response.TransactionResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactsViewModel(private val repository: Repository) : ViewModel() {

    private val _getUsersLiveData: MutableLiveData<List<User>> = MutableLiveData()
    val getUsersLiveData: LiveData<List<User>> = _getUsersLiveData

    val transactionLiveData: MutableLiveData<Receipt> = MutableLiveData()

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message

    fun getUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    repository.getUsers { result ->
                        when (result) {
                            is ServiceCallBack.Success -> {
                                val users: MutableList<User> = mutableListOf()
                                for (user in result.users) {
                                    val person = User(
                                        id = user.id,
                                        name = user.name,
                                        username = user.username,
                                        img = user.img
                                    )
                                    users.add(person)
                                }
                                _getUsersLiveData.value = users
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
                _message.value = e.toString()
            }
        }
    }

    fun transactionEntryUser(transaction: Transaction) {
        ApiService.serviceInterface.sendTransactionEntryUsers(transaction)
            .enqueue(object : Callback<TransactionResponse> {

                override fun onResponse(
                    call: Call<TransactionResponse>,
                    response: Response<TransactionResponse>
                ) {
                    response.body()?.let { transactionResponse ->
                        Log.i(
                            "SUCCESS", transactionResponse.transaction?.id.toString() + " " +
                                    transactionResponse.transaction?.timestamp + " " +
                                    transactionResponse.transaction?.value + " " +
                                    transactionResponse.transaction?.destination_user.toString() +
                                    transactionResponse.transaction?.status + " " +
                                    transactionResponse.transaction?.success
                        )

                    }
                    Log.i("SUCCESS", response.message())
                    transactionLiveData.value = response.body()?.transaction
                }

                override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                    Log.i("FAILURE", t.message)
                    _message.value = t.message
                }
            })
    }
}