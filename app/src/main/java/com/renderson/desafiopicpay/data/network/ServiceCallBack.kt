package com.renderson.desafiopicpay.data.network

import com.renderson.desafiopicpay.data.network.response.UsersResponse

sealed class ServiceCallBack {
    class Success(val users: List<UsersResponse>) : ServiceCallBack()
    class ApiError(val statusCode: Int) : ServiceCallBack()
    object ServerError : ServiceCallBack()
}