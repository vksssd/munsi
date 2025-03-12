package com.vksssd.alpha.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vksssd.alpha.data.converter.Converters
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Long = 0,
    val amount: Double,
//    val merchant: String,
    val status: String,
    val timestamp: Date,
    val appName: String ?= "ALPHA",
    val notes: String? = null,
    val category: String? = null,
    val type: TransactionType = TransactionType.entries.find { it.name == "CASH_IN" } ?: TransactionType.CASH_IN)

enum class TransactionType {
    CASH_IN,
    BILL_PAYMENT,
    UPI_PAYMENT,
    REFUND
}