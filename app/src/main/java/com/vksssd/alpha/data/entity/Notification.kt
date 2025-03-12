package com.vksssd.alpha.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val type: NotificationType,
    val date: Date,
    val isRead: Boolean = false,
    val isSynced: Boolean = false
)

enum class NotificationType {
    BILL_CREATED,
    BILL_PAID,
    LOW_STOCK,
    PAYMENT_DUE,
    SYSTEM_UPDATE
}