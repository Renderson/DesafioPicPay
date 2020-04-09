package com.renderson.desafiopicpay.presentation.contacts.adapter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.renderson.desafiopicpay.data.PicPayService
import com.renderson.desafiopicpay.data.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ContactsViewModel: ViewModel() {

    val contactsLiveData: MutableLiveData<List<User>> = MutableLiveData()

    fun getUsers() {

        PicPayService.serviceInterface.searchUsers().enqueue(object : Callback<List<User>>{

            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
               if (response.isSuccessful){
                        val users: MutableList<User> = mutableListOf()

                   response.body()?.let { UsersResponse ->
                       for(result in UsersResponse){
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

            }
        })
    }
}