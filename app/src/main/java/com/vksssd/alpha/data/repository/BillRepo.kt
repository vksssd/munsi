package com.vksssd.alpha.data.repository

import com.vksssd.alpha.data.dao.BillDao
import com.vksssd.alpha.data.entity.Bill
import com.vksssd.alpha.data.entity.BillStatus
import com.vksssd.alpha.data.entity.BillType
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillRepo @Inject constructor(
    private val billDao: BillDao,
//    private val billItemDao: BillItemDao,
//    private val promoCodeDao: PromoCodeDao,
    ) {

    fun getAllBills(): Flow<List<Bill>> = billDao.getAllBills()

    private suspend fun getBillById(id: Long): Bill? = billDao.getBillById(id)

    private suspend fun insertBill(bill: Bill): Long = billDao.insertBill(bill)

    private suspend fun updateBill(bill: Bill) = billDao.updateBill(bill)

    suspend fun deleteBill(bill: Bill) = billDao.deleteBill(bill)

    fun getBillByStatus(type: String, status: String): Flow<List<Bill>> = billDao.getBillsByTypeAndStatus(type,status)

    fun getBillByDateRange(type: String, startDate: Long, endDate: Long): Flow<List<Bill>> = billDao.getBillsByTypeAndDateRange(type,startDate,endDate)


    suspend fun createDirectCashBill(
        customerName: String?,
        totalAmount: Double,
        discount: Double = 0.0,
        finalAmount: Double? = null,
        promoCodeId: Long? = null,
        isSynced: Boolean = false,
        status: BillStatus = BillStatus.PENDING
    ):Long {
        val bill = Bill(
            customerName = customerName,
            billType = BillType.DIRECT_CASH,
            billDate = Date(),
            billAmount = totalAmount,
            billFinalAmount = finalAmount,
            discount = discount,
            promoCodeId = promoCodeId,
            isSynced = isSynced,
            status = status
        )

        return insertBill(bill)

    }


    suspend fun updateBillStatus(billId: Long, status: BillStatus) {
        val bill = getBillById(billId)?.copy(status = status)
        bill?.let { updateBill(it) }
    }

//    fun getBillItems(billId: Long): Flow<List<BillItem>> = billItemDao.getBillItems(billId)

//    suspend fun deleteBillItems(billId: Long) = billItemDao.deleteBillItems(billId)


}