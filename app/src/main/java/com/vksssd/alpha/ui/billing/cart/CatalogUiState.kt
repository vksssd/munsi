package com.vksssd.alpha.ui.billing.cart

import com.vksssd.alpha.data.entity.Category
import com.vksssd.alpha.data.entity.Product

data class CatalogUiState (
    val categories: List<Category> = emptyList(),
    val products: List<Product> = emptyList(),
    val selectedProducts: Map<Product, Int> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)