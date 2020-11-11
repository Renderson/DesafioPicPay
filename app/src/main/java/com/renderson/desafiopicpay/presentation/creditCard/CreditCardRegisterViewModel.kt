package com.renderson.desafiopicpay.presentation.creditCard

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.renderson.desafiopicpay.data.database.entity.CardEntity
import com.renderson.desafiopicpay.data.database.repository.CreditCardRepository
import kotlinx.coroutines.launch

class CreditCardRegisterViewModel(private val repository: CreditCardRepository) : ViewModel() {

    private val _cardStateEventData = MutableLiveData<CreditCardState>()
    val cardStateEventData: LiveData<CreditCardState>
        get() = _cardStateEventData

    private val _getCarEvent = MutableLiveData<List<CardEntity?>>()
    val getCardEvent: LiveData<List<CardEntity?>>
        get() = _getCarEvent

    fun getCreditCard() = viewModelScope.launch {
        _getCarEvent.postValue(repository.getCard())
    }

    fun saveCreditCard(cardNumber: String, name: String, expiration: String, cvv: String) =
        viewModelScope.launch {
            try {
                repository.delete()
                repository.save(cardNumber, name, expiration, cvv)
                _cardStateEventData.value = CreditCardState.Saved
            } catch (ex: Exception) {
                Log.e(TAG, ex.toString())
            }
        }

    sealed class CreditCardState {
        object Saved : CreditCardState()
        object GetAll : CreditCardState()
        object Deleted : CreditCardState()
    }
}