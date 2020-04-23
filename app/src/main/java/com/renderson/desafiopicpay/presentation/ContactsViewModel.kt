package com.renderson.desafiopicpay.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.renderson.desafiopicpay.data.model.Transaction
import com.renderson.desafiopicpay.data.model.User
import com.renderson.desafiopicpay.data.network.PicPayService
import com.renderson.desafiopicpay.data.model.Receipt
import com.renderson.desafiopicpay.data.network.response.TransactionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactsViewModel : ViewModel() {

    val contactsLiveData: MutableLiveData<List<User>> = MutableLiveData()
    val transactionLiveData: MutableLiveData<Receipt> = MutableLiveData()
    val message: MutableLiveData<String> = MutableLiveData()

    fun getUsers() {
        PicPayService.serviceInterface.searchUsers().enqueue(object : Callback<List<User>> {

            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users: MutableList<User> = mutableListOf()

                    response.body()?.let { UsersResponse ->
                        for (result in UsersResponse) {
                            val user = User(
                                id = result.id,
                                name = result.name,
                                username = result.username,
                                img = result.img
                            )
                            users.add(user)
                        }
                    }
                    contactsLiveData.value = users
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                message.value = "Error: $t"
            }
        })
    }

    fun transactionEntryUser(transaction: Transaction) {
        PicPayService.serviceInterface.sendTransactionEntryUsers(transaction)
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
                }
            })
    }

    fun showMessage(): LiveData<String> {
        return message
    }
}