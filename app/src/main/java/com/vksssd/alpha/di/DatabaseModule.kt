package com.vksssd.alpha.di

import android.content.Context
import androidx.room.Room
import com.vksssd.alpha.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "alpha_app_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesBillDao(database: AppDatabase) = database.billDao()

    @Provides
    fun providesTransactionDao(database: AppDatabase) = database.transactionDao()

    @Provides
    fun providesProductDao(database: AppDatabase) = database.productDao()

    @Provides
    fun providesCategoryDao(database: AppDatabase) = database.categoryDao()

    @Provides
    fun providesUserProfileDao(database: AppDatabase) = database.userProfileDao()

    @Provides
    fun providesNotificationDao(database: AppDatabase) = database.notificationDao()
}