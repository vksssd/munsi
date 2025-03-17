package com.vksssd.alpha.ui.inventory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vksssd.alpha.data.entity.Category
import com.vksssd.alpha.data.entity.Transaction
import com.vksssd.alpha.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: Flow<List<Category>> = _categories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadCategories()
    }


    fun addNewCategory(category: Category) {
        Log.d("NewCategoryViewModel", "addNewCategories: $category")
        viewModelScope.launch {
            productRepository.addNewCategory(category)
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
                    Log.d("CategoryViewModel", "Loaded ${fetchedCategories.size} categories")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load category: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}