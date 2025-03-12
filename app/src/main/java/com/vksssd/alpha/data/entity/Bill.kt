package com.vksssd.alpha.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

enum class BillType{
    DIRECT_CASH,
    CART_BASED,
}

@Entity(tableName = "bills")
data class Bill(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerName: String ?= null,
    val billType: BillType,
    val billDate: Date,
    val billAmount: Double,
    val billFinalAmount: Double?,
    val discount: Double,
    val promoCodeId: Long? = null,
    val isSynced: Boolean = false,
    val status: BillStatus
)

enum class BillStatus {
    PENDING,
    PAID,
    CANCELLED,
    REFUNDED
}