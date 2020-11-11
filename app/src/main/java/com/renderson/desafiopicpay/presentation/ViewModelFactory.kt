package com.renderson.desafiopicpay.presentation

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.renderson.desafiopicpay.data.database.AppDatabase
import com.renderson.desafiopicpay.data.database.dao.CreditCardDAO
import com.renderson.desafiopicpay.data.database.repository.CreditCardRepository
import com.renderson.desafiopicpay.data.database.repository.DatabaseDataSource
import com.renderson.desafiopicpay.data.network.repository.Repository
import com.renderson.desafiopicpay.presentation.contacts.ContactsViewModel
import com.renderson.desafiopicpay.presentation.creditCard.CreditCardRegisterViewModel
import com.renderson.desafiopicpay.presentation.transaction.TransactionViewModel

class ViewModelFactory(private val dataSource: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java) ||
            modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return modelClass.getConstructor(Repository::class.java)
                .newInstance(dataSource)
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ViewModelFactoryDB(private val context: Activity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val creditCardDAO: CreditCardDAO =
            AppDatabase.getInstance(context).creditCardDAO

        val repository: CreditCardRepository = DatabaseDataSource(creditCardDAO)
        return CreditCardRegisterViewModel(repository) as T
    }
}