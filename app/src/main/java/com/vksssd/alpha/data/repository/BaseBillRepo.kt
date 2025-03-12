package com.vksssd.alpha.data.repository

import com.vksssd.alpha.data.entity.Bill
import com.vksssd.alpha.data.entity.BillStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date


interface BaseBillRepo {
    fun getAllBills(): Flow<List<Bill>>
    suspend fun getBillById(id: Long): Bill?
    suspend fun insertBill(bill: Bill): Long
    suspend fun updateBill(bill: Bill)
    suspend fun deleteBill(bill: Bill)
    fun getBillsByStatus(status: BillStatus): Flow<List<Bill>>
    fun getBillsByDateRange(startDate: Date, endDate: Date): Flow<List<Bill>>
}