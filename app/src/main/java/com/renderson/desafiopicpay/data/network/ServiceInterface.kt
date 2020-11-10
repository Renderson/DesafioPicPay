package com.renderson.desafiopicpay.data.network

import com.renderson.desafiopicpay.data.model.Transaction
import com.renderson.desafiopicpay.data.model.User
import com.renderson.desafiopicpay.data.network.response.TransactionResponse
import com.renderson.desafiopicpay.data.network.response.UsersResponse
import retrofit2.Call
import retrofit2.http.*

interface ServiceInterface {

    @GET("users")
    fun getUsers(): Call<List<UsersResponse>>

    @POST("transaction")
    fun sendTransactionEntryUsers(@Body transaction: Transaction) : Call<TransactionResponse>
}
