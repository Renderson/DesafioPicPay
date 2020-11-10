package com.renderson.desafiopicpay.data.network.repository

import com.renderson.desafiopicpay.data.model.Transaction
import com.renderson.desafiopicpay.data.network.ServiceCallBack
import com.renderson.desafiopicpay.data.network.ServiceInterface
import com.renderson.desafiopicpay.data.network.response.TransactionResponse
import com.renderson.desafiopicpay.data.network.response.UsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceApiDataSource(private val service: ServiceInterface) : Repository {

    override suspend fun getUsers(resultCallback: (serviceCallBack: ServiceCallBack) -> Unit) {
        try {
            service.getUsers().enqueue(object : Callback<List<UsersResponse>> {
                override fun onResponse(
                    call: Call<List<UsersResponse>>,
                    response: Response<List<UsersResponse>>
                ) {
                    when {
                        response.isSuccessful -> {
                            val users: MutableList<UsersResponse> = mutableListOf()

                            response.body()?.let { usersResponse ->
                                for (result in usersResponse) {
                                    users.add(result)
                                }
                            }
                            resultCallback(ServiceCallBack.Success(users))
                        }
                        else -> resultCallback(ServiceCallBack.ApiError(response.code()))
                    }
                }

                override fun onFailure(call: Call<List<UsersResponse>>, t: Throwable) {
                    resultCallback(ServiceCallBack.ServerError)
                }
            })
        } catch (e: Exception) {
            resultCallback(ServiceCallBack.ServerError)
        }
    }

    override suspend fun transaction(
        transaction: Transaction, resultCallback: (serviceCallBack: ServiceCallBack) -> Unit
    ) {
        try {
            service.sendTransaction(transaction)
                .enqueue(object : Callback<TransactionResponse> {

                    override fun onResponse(
                        call: Call<TransactionResponse>,
                        response: Response<TransactionResponse>
                    ) {
                        when {
                            response.isSuccessful -> {
                                resultCallback(ServiceCallBack.SuccessTransaction(response.body()?.transaction))
                            }
                            else -> resultCallback(ServiceCallBack.ApiError(response.code()))
                        }
                    }

                    override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                        resultCallback(ServiceCallBack.ServerError)
                    }
                })
        } catch (e: Exception) {
            resultCallback(ServiceCallBack.ServerError)
        }
    }
}