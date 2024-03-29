package com.renderson.desafiopicpay.presentation.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.renderson.desafiopicpay.data.model.User
import com.renderson.desafiopicpay.data.network.ServiceCallBack
import com.renderson.desafiopicpay.data.network.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsViewModel(private val repository: Repository) : ViewModel() {

    private val _getUsersLiveData: MutableLiveData<List<User>> = MutableLiveData()
    val getUsersLiveData: LiveData<List<User>> = _getUsersLiveData

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
}