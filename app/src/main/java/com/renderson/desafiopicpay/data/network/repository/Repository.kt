package com.renderson.desafiopicpay.data.network.repository

import com.renderson.desafiopicpay.data.model.Transaction
import com.renderson.desafiopicpay.data.network.ServiceCallBack

interface Repository {

    suspend fun getUsers(resultCallback: (serviceCallBack: ServiceCallBack) -> Unit)

    suspend fun transaction(transaction: Transaction, resultCallback: (serviceCallBack: ServiceCallBack) -> Unit)
}