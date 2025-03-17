package com.vksssd.alpha.ui.main.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vksssd.alpha.data.entity.Transaction
import com.vksssd.alpha.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val transactionRepo: TransactionRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: Flow<List<Transaction>> = _transactions

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadHistory()
    }

    private fun loadHistory() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                transactionRepo.getAllTransactions().collectLatest { fetchedTransactions ->
                    _transactions.update { fetchedTransactions }
                    _errorMessage.value = null
                    _isLoading.value = false
                    Log.d("HistoryViewModel", "Loaded ${fetchedTransactions.size} transactions")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load history: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}