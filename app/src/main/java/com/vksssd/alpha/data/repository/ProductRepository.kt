package com.vksssd.alpha.data.repository

import android.util.Log
import com.vksssd.alpha.data.dao.CategoryDao
import com.vksssd.alpha.data.dao.ProductDao
import com.vksssd.alpha.data.entity.Category
import com.vksssd.alpha.data.entity.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
){
//    products

    fun getAllProducts(): Flow<List<Product>> = flow {
        Log.d("ProductRepo", "getAllProduct() called")

        productDao.getAllProducts().collect { products ->
            if(products.isEmpty()){
                Log.d("ProductRepo", "Empty Products")
            }
            if (products.isNotEmpty()) {
                Log.d("ProductRepo", "Products: $products")
                emit(products)
            }
        }
    }.conflate()

    suspend fun addNewProduct(product: Product): Long = productDao.insertProduct(product)

    suspend fun getProductById(id: Long): Product? = productDao.getProductById(id)

    fun getProductByCategory(categoryId: Long): Flow<List<Product>> = flow {
        Log.d("ProductRepo", "getProductByCategory() called")

        productDao.getProductsByCategory(categoryId).collect { products ->
            if(products.isEmpty()){
                Log.d("ProductRepo", "Empty Products")
            }
            if (products.isNotEmpty()) {
                Log.d("ProductRepo", "Products: $products")
                emit(products)
            }
        }
    }.conflate()

//    categories

    fun getAllCategories(): Flow<List<Category>> = flow{

    categoryDao.getAllCategories().collect{
        categories ->
        if(categories.isEmpty()){
            Log.d("CategoryRepo", "Empty Categories")
        }
        if(categories.isNotEmpty()){
            Log.d("CategoryRepo", "Categories: $categories")
            emit(categories)
            }
        }
    }.conflate()


    suspend fun addNewCategory(category: Category): Long = categoryDao.insertCategory(category)

    suspend fun getCategoryById(id: Long): Category? = categoryDao.getCategoryById(id)




}