package com.vksssd.alpha.data.repository

import com.vksssd.alpha.data.dao.BillDao
import com.vksssd.alpha.data.entity.Bill
import com.vksssd.alpha.data.entity.BillStatus
import com.vksssd.alpha.data.entity.BillType
import com.vksssd.alpha.data.entity.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CashBillRepo @Inject constructor(
    private val billDao: BillDao,
    private val transactionRepository: TransactionRepository
    ):BaseBillRepo {
    override fun getAllBills(): Flow<List<Bill>> = billDao.getBillsByType(BillType.DIRECT_CASH.name)

    override suspend fun getBillById(id: Long): Bill ?= billDao.getBillById(id)

    override suspend fun insertBill(bill: Bill): Long = billDao.insertBill(bill)

    override suspend fun updateBill(bill: Bill) = billDao.updateBill(bill)

    override suspend fun deleteBill(bill: Bill) = billDao.deleteBill(bill)

    override fun getBillsByStatus(status: BillStatus): Flow<List<Bill>> =
        billDao.getBillsByTypeAndStatus(BillType.DIRECT_CASH.name, status.name)

    override fun getBillsByDateRange(startDate: Date, endDate: Date): Flow<List<Bill>> =
        billDao.getBillsByTypeAndDateRange(BillType.DIRECT_CASH.name, startDate.time, endDate.time)

    suspend fun createCashBill(
        customerName: String?,
        totalAmount: Double,
        discount: Double = 0.0,
        promoCodeId: Long? = null,
        isSynced: Boolean = false,
        finalAmount: Double? = null,
        status: BillStatus = BillStatus.PENDING
    ): Long {
        val bill = Bill(
            customerName = customerName,
            billAmount = totalAmount,
            billDate = Date(),
            status = status,
            billType = BillType.DIRECT_CASH,
            billFinalAmount = finalAmount,
            discount = 0.0,
            isSynced = false
        )
        return insertBill(bill)
    }

    suspend fun addTransaction(transaction: Transaction){
        transactionRepository.insertTransaction(transaction)
     }

}