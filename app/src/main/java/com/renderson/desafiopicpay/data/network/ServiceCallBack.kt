package com.renderson.desafiopicpay.data.network

import com.renderson.desafiopicpay.data.model.Receipt
import com.renderson.desafiopicpay.data.network.response.UsersResponse

sealed class ServiceCallBack {
    class Success(val users: List<UsersResponse>) : ServiceCallBack()
    class SuccessTransaction(val transaction: Receipt?) : ServiceCallBack()
    class ApiError(val statusCode: Int) : ServiceCallBack()
    object ServerError : ServiceCallBack()
}