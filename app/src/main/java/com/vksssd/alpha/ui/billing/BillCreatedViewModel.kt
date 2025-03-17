package com.vksssd.alpha.ui.billing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.vksssd.alpha.data.entity.Bill
import com.vksssd.alpha.data.entity.Transaction
import com.vksssd.alpha.data.repository.CashBillRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillCreatedViewModel @Inject constructor(
    private val cashBillRepo: CashBillRepo
): ViewModel() {

    val allBills: LiveData<List<Bill>> = cashBillRepo.getAllBills().asLiveData()

    fun addBill(bill: Bill){
        viewModelScope.launch {
            cashBillRepo.insertBill(bill)
        }
        Log.e("bill","bill added $bill")
        addTransaction(bill)
    }

    private fun addTransaction(bill: Bill){
        viewModelScope.launch {
            cashBillRepo.addTransaction(transaction = Transaction(
                amount = bill.billAmount,
                status = "paid",
                timestamp = bill.billDate
            )
            )
            Log.e("transaction","transaction added $bill")
        }
    }


}