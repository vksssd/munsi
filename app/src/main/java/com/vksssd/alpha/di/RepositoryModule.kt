package com.vksssd.alpha.di

import com.vksssd.alpha.data.repository.BillRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBillRepository(
        billRepositoryImpl: BillRepo
    ): BillRepo

//    @Binds
//    abstract fun bindTransactionItemRepository(
//        transactionRepositoryImpl: TransactionRepository
//    ): TransactionRepository // Corrected this line

}