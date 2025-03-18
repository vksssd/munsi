package com.vksssd.alpha.ui.billing.cart

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: Flow<List<Category>> = _categories

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: Flow<List<Product>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        observeCategories()
        loadCategories()
        // Load initial products for a default category (e.g., ID = 1)
        getProductByCategoryId(1)
    }

    private fun observeCategories() {
        viewModelScope.launch {
            categories.collectLatest { categories ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(categories = categories)
                }
                if (categories.isNotEmpty()) {
                    onUiEvent(CatalogUiEvent.OnCategorySelected(categories.first().id))
                }
            }
        }
    }

    fun onUiEvent(event: CatalogUiEvent) {
        when (event) {
            is CatalogUiEvent.OnCategorySelected -> {
                fetchedProductsForCategory(event.categoryId)
            }
            is CatalogUiEvent.OnProductSelected -> {
                toggleProductSelection(event.product)
            }
            is CatalogUiEvent.ProductCountChanged -> {
                updateProductCount(event.product, event.count)
            }
            is CatalogUiEvent.ProceedToCheckout -> {
                Log.d("CatalogViewModel", "Proceed to Order clicked")
            }
            is CatalogUiEvent.OnBackClicked -> {
                // Handle back button if needed
            }
            CatalogUiEvent.ClearErrorMessage -> { /* Implementation if needed */ }
            CatalogUiEvent.OnSearchQueryChanged -> { /* Implementation if needed */ }
        }
    }

    private fun fetchedProductsForCategory(categoryId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                getProductByCategoryId(categoryId)
                products.collectLatest { products ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            products = products,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        errorMessage = "Failed to load products: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun toggleProductSelection(product: Product) {
        _uiState.update { currentUiState ->
            val currentCount = currentUiState.selectedProducts[product] ?: 0
            val updatedMap = currentUiState.selectedProducts.toMutableMap()
            if (currentCount > 0) {
                updatedMap.remove(product)
            } else {
                updatedMap[product] = 1
            }
            currentUiState.copy(selectedProducts = updatedMap)
        }
    }

    private fun updateProductCount(product: Product, count: Int) {
        _uiState.update { currentUiState ->
            val updatedMap = currentUiState.selectedProducts.toMutableMap()
            if (count > 0) {
                updatedMap[product] = count
            } else {
                updatedMap.remove(product)
            }
            currentUiState.copy(selectedProducts = updatedMap)
        }
    }

    private fun loadCategories() {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                productRepository.getAllCategories().collectLatest { fetchedCategories ->
                    _categories.update { fetchedCategories }
                    _errorMessage.value = null
                    _isLoading.value = false
                    Log.d("CatalogViewModel", "Loaded ${fetchedCategories.size} categories")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load category: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getProductByCategoryId(categoryId: Long) {
        viewModelScope.launch {
            try {
                productRepository.getProductByCategory(categoryId).collectLatest { fetchedProducts ->
                    _products.update { fetchedProducts }
                    Log.d("CatalogViewModel", "Loaded ${fetchedProducts.size} products")
                }
            } catch (e: Exception) {
                Log.d("CatalogViewModel", "Failed to load product: ${e.message}")
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
                    Log.d("CatalogViewModel", "Loaded ${fetchedProducts.size} products")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load product: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
