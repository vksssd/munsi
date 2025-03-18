package com.vksssd.alpha.ui.billing.cart

import com.vksssd.alpha.data.entity.Product

sealed class CatalogUiEvent {
    data class OnCategorySelected(val categoryId: Long) : CatalogUiEvent()
    data class OnProductSelected(val product: Product) : CatalogUiEvent()
    data class ProductCountChanged(val product: Product, val count: Int) : CatalogUiEvent()
    data object ProceedToCheckout : CatalogUiEvent()
    data object OnBackClicked : CatalogUiEvent()
    data object OnSearchQueryChanged : CatalogUiEvent()
    data object ClearErrorMessage : CatalogUiEvent()
}