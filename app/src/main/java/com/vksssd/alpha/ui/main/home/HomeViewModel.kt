package com.vksssd.alpha.ui.main.home

import android.util.Log
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
class HomeViewModel @Inject constructor(
    private val transactionRepo: TransactionRepository
): ViewModel() {
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: Flow<List<Transaction>> = _transactions

    init {
        loadLastTransactions()
    }

    private fun loadLastTransactions() {
        viewModelScope.launch {
            try {
                transactionRepo.getLastNTransactions(4).collectLatest { fetchedTransactions ->
                _transactions.update { fetchedTransactions }
                Log.d("HistoryViewModel", "Loaded ${fetchedTransactions.size} transactions")
            }
        } catch (e: Exception) {
            Log.d("HistoryViewModel", "No transactions found")
        }
        }
    }
}