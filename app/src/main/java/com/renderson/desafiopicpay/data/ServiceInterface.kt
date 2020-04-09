package com.renderson.desafiopicpay.data

import com.renderson.desafiopicpay.data.model.User
import retrofit2.Call
import retrofit2.http.GET

interface ServiceInterface {

    @GET("users")
    fun searchUsers(): Call<List<User>>
}
