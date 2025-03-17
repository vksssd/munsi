package com.vksssd.alpha.data.dao

import androidx.room.*
import com.vksssd.alpha.data.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM products WHERE productName LIKE :query")
    fun searchProducts(query: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE isActive = 1")
    fun getActiveProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE isActive = 0")
    fun getInactiveProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    fun getProductsByCategory(categoryId: Long): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE productName LIKE :query AND isActive = 1")
    fun searchActiveProducts(query: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE productName LIKE :query AND isActive = 0")
    fun searchInactiveProducts(query: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE categoryId = :categoryId AND isActive = 1")
    fun getActiveProductsByCategory(categoryId: Long): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE categoryId = :categoryId AND isActive = 0")
    fun getInactiveProductsByCategory(categoryId: Long): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE productName LIKE :query AND categoryId = :categoryId AND isActive = 1")
    fun searchActiveProductsByCategory(query: String, categoryId: Long): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE productName LIKE :query AND categoryId = :categoryId AND isActive = 0")
    fun searchInactiveProductsByCategory(query: String, categoryId: Long): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE productName LIKE :query AND isActive = 1 ORDER BY price ASC")
    fun searchActiveProductsByPriceAsc(query: String): Flow<List<Product>>

//    get products with categories
//    @Transaction
//    @Query("SELECT * FROM products")
//    fun getProductsWithCategories(): Flow<List<ProductWithCategory>>


} 