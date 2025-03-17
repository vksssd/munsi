package com.vksssd.alpha.data.dao

import androidx.room.*
import com.vksssd.alpha.data.entity.Transaction
import com.vksssd.alpha.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC LIMIT :count")
    fun getLastNTransactions(count: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE appName = :appName ORDER BY timestamp DESC")
    fun getTransactionsByApp(appName: String): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE :query || '%' ORDER BY timestamp DESC")
    fun searchTransactions(query: String): Flow<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getTotalAmountByDateRange(startDate: Date, endDate: Date): Flow<Double?>

    @Query("SELECT * FROM transactions WHERE transactionId = :transactionId")
    suspend fun getTransactionById(transactionId: String): Transaction?

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()

    // Category-related queries
    @Query("SELECT DISTINCT category FROM transactions WHERE category IS NOT NULL")
    fun getAllCategories(): Flow<List<String>>

    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY timestamp DESC")
    fun getTransactionsByCategory(category: String): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY timestamp DESC")
    fun getTransactionsByType(type: String): Flow<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE category = :category AND timestamp BETWEEN :startDate AND :endDate")
    fun getTotalAmountByCategory(category: String, startDate: Date, endDate: Date): Flow<Double?>

    // Tag-related queries
//    @Query("SELECT * FROM transactions WHERE tags LIKE '%' || :tag || '%' ORDER BY timestamp DESC")
//    fun getTransactionsByTag(tag: String): Flow<List<Transaction>>

//    @Query("SELECT DISTINCT tags FROM transactions WHERE tags IS NOT NULL")
//    fun getAllTags(): Flow<List<String>>

    // Statistics queries
    @Query("""
        SELECT appName, SUM(amount) as totalAmount, COUNT(*) as transactionCount 
        FROM transactions 
        WHERE timestamp BETWEEN :startDate AND :endDate 
        GROUP BY appName
    """)
    fun getAppStatistics(startDate: Date, endDate: Date): Flow<List<AppStatistics>>

    @Query("""
        SELECT category, SUM(amount) as totalAmount, COUNT(*) as transactionCount 
        FROM transactions 
        WHERE timestamp BETWEEN :startDate AND :endDate 
        AND category IS NOT NULL 
        GROUP BY category
    """)
    fun getCategoryStatistics(startDate: Date, endDate: Date): Flow<List<CategoryStatistics>>

    @Query("""
        SELECT type, SUM(amount) as totalAmount, COUNT(*) as transactionCount 
        FROM transactions 
        WHERE timestamp BETWEEN :startDate AND :endDate 
        GROUP BY type
    """)
    fun getTypeStatistics(startDate: Date, endDate: Date): Flow<List<TypeStatistics>>

    @Query("""
        SELECT strftime('%Y-%m', timestamp/1000, 'unixepoch') as month,
               SUM(amount) as totalAmount,
               COUNT(*) as transactionCount
        FROM transactions
        WHERE timestamp BETWEEN :startDate AND :endDate
        GROUP BY month
        ORDER BY month
    """)
    fun getMonthlyStatistics(startDate: Date, endDate: Date): Flow<List<MonthlyStatistics>>
}

data class AppStatistics(
    val appName: String,
    val totalAmount: Double,
    val transactionCount: Int
)

data class CategoryStatistics(
    val category: String,
    val totalAmount: Double,
    val transactionCount: Int
)

data class TypeStatistics(
    val type: TransactionType,
    val totalAmount: Double,
    val transactionCount: Int
)

data class MonthlyStatistics(
    val month: String,
    val totalAmount: Double,
    val transactionCount: Int
)