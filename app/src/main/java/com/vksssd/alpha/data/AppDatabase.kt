package com.vksssd.alpha.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vksssd.alpha.data.converter.Converters
import com.vksssd.alpha.data.dao.*
import com.vksssd.alpha.data.entity.*

@Database(
    entities = [
        Transaction::class,
        Bill::class,
        Product::class,
        UserProfile::class,
        Notification::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun billDao(): BillDao
    abstract fun productDao(): ProductDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun notificationDao(): NotificationDao
}