package com.vksssd.alpha.ui.inventory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vksssd.alpha.data.entity.Category
import com.vksssd.alpha.data.entity.Product
import com.vksssd.alpha.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: Flow<List<Product>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
//        loadProducts()
        getProductByCategoryId(1)
    }


    fun addNewProduct(product: Product) {
        Log.d("NewProductViewModel", "addNewProduct: $product")
        viewModelScope.launch {
            productRepository.addNewProduct(product)
        }
    }

    fun getProductByCategoryId(categoryId: Long) {
            viewModelScope.launch {
                try {
                    productRepository.getProductByCategory(categoryId).collectLatest { fetchedProducts ->
                        _products.update { fetchedProducts }

                        Log.d("ProductViewModel", "Loaded ${fetchedProducts.size} products")
                    }
                } catch (e: Exception) {
                    Log.d("ProductViewModel", "Failed to load product: ${e.message}")
                }
        }
    }


     fun loadProducts() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                productRepository.getAllProducts().collectLatest { fetchedProducts ->
                    _products.update { fetchedProducts }
                    _errorMessage.value = null
                    _isLoading.value = false
                    Log.d("ProductViewModel", "Loaded ${fetchedProducts.size} products")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load product: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}