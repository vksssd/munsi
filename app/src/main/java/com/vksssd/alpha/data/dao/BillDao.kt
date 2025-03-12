package com.vksssd.alpha.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vksssd.alpha.data.entity.Bill
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    @Query("SELECT * FROM bills Order By billDate DESC")
    fun getAllBills(): Flow<List<Bill>>

    @Query("SELECT * FROM bills WHERE billType = :type Order By billDate DESC")
    fun getBillsByType(type: String): Flow<List<Bill>>

    @Query("SELECT * FROM bills WHERE id = :id")
    fun getBillById(id: Long): Bill?

    @Query("SELECT * FROM bills WHERE customerName = :customerName")
    fun getBillsByCustomerName(customerName: String): Flow<List<Bill>>

    @Query("SELECT * FROM bills WHERE billType = :type AND status = :status ORDER BY billDate DESC")
    fun getBillsByTypeAndStatus(type: String, status: String): Flow<List<Bill>>

    @Query("SELECT * FROM bills WHERE billType = :type AND billDate BETWEEN :startDate AND :endDate ORDER BY billDate DESC")
    fun getBillsByTypeAndDateRange(type: String, startDate: Long, endDate: Long): Flow<List<Bill>>

    @Query("SELECT * FROM bills WHERE promoCodeId = :promoCodeId")
    fun getBillsByPromoCode(promoCodeId: Long): Flow<List<Bill>>

    @Update
    suspend fun updateBill(bill: Bill)

    @Delete
    suspend fun deleteBill(bill: Bill)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: Bill): Long

    @Query("SELECT SUM(billFinalAmount) FROM bills WHERE status = 'PAID' AND billDate BETWEEN :startDate AND :endDate")
    fun getTotalSalesByDateRange(startDate: Long, endDate: Long): Flow<Double?>


}