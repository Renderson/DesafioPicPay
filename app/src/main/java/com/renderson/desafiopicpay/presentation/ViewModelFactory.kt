package com.renderson.desafiopicpay.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.renderson.desafiopicpay.data.network.repository.Repository
import com.renderson.desafiopicpay.presentation.contacts.ContactsViewModel

class ViewModelFactory(private val dataSource: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            return modelClass.getConstructor(Repository::class.java)
                .newInstance(dataSource)
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}