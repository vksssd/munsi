package com.vksssd.alpha.data.repository

import android.util.Log
import com.vksssd.alpha.data.dao.TransactionDao
import com.vksssd.alpha.data.entity.Transaction as TransactionEntity
import com.vksssd.alpha.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
){
//    fun getAllTransactions(): Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()

    fun getAllTransactions(): Flow<List<TransactionEntity>> = flow {
        Log.d("TransactionRepo", "getAllTransactions() called")

        transactionDao.getAllTransactions().collect { transactions ->
            if(transactions.isEmpty()){
                Log.d("TransactionRepo", "Empty Transactions")
            }
            if (transactions.isNotEmpty()) {
                Log.d("TransactionRepo", "Transactions: $transactions")
                emit(transactions)
            }
        }
    }.conflate()

    fun getLastNTransactions(count: Int):  Flow<List<TransactionEntity>> = flow {
        Log.d("TransactionRepo", "getLastNTransactions() called")

        transactionDao.getLastNTransactions(count).collect { transactions ->
            if(transactions.isEmpty()){
                Log.d("TransactionRepo", "Empty Transactions")
            }
            if (transactions.isNotEmpty()) {
                Log.d("TransactionRepo", "Transactions: $transactions")
                emit(transactions)
            }
        }
    }.conflate()
//        transactionDao.getLastNTransactions(4)

    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsByDateRange(startDate, endDate)

    fun getTransactionsByApp(appName: String): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsByApp(appName)

    fun searchTransactions(query: String): Flow<List<TransactionEntity>> =
        transactionDao.searchTransactions(query)

    fun getTotalAmountByDateRange(startDate: Date, endDate: Date): Flow<Double?> =
        transactionDao.getTotalAmountByDateRange(startDate, endDate)

//    suspend fun savePaymentInfo(paymentInfo: PaymentInfo) {
//        val transaction = Transaction(
//            transactionId = paymentInfo.transactionId,
//            amount = paymentInfo.amount,
//            merchant = paymentInfo.merchant,
//            status = paymentInfo.status,
//            timestamp = Date(paymentInfo.timestamp),
//            appName = paymentInfo.appName
//        )
//        transactionDao.insertTransaction(transaction)
//    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun deleteAllTransactions() {
        transactionDao.deleteAllTransactions()
    }

    suspend fun getTransactionById(transactionId: String): TransactionEntity? =
        transactionDao.getTransactionById(transactionId)

    suspend fun insertTransaction(transaction: TransactionEntity): Unit = transactionDao.insertTransaction(transaction)

    fun getTransactionsByType(type: TransactionType): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsByType(type.name)


    suspend fun createTransaction(
        amount: Double,
        description: String,
        appName: String?,
        category: String?,
//        tags: List<String>?,
        type: TransactionType?
    ): Unit? {
        // Create the TransactionEntity
        val transactionEntity = type?.let {
            TransactionEntity(
                transactionId = 0,// It will be generated automatically by the database
                amount = amount,
                status = "Done",
                timestamp = Date(),
                notes = description,
                appName = appName,
                category = category,
//                tags = tags ?: emptyList(),// Default to an empty list if tags is null
                type = it
            )
        }
        return transactionEntity?.let { transactionDao.insertTransaction(it) }
    }
}