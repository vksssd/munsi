package com.vksssd.alpha.ui.billing.cart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vksssd.alpha.R
import com.vksssd.alpha.data.entity.Category
import com.vksssd.alpha.data.entity.Product
import com.vksssd.alpha.data.entity.SelectedItem
import com.vksssd.alpha.databinding.FragmentCatalogBinding
import com.vksssd.alpha.ui.inventory.CategoryAdapter
import com.vksssd.alpha.ui.inventory.ProductAdapter
import com.vksssd.alpha.ui.inventory.SelectionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch



@AndroidEntryPoint
class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null


    private val catalogViewModel: CatalogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.catalogTitlebar.toolbarTitle.text = "Catalog"
        binding.catalogTitlebar.searchButton.visibility = View.VISIBLE
        binding.proceedOrderLv.visibility = View.GONE
        binding.catalogTitlebar.backButton.visibility = View.VISIBLE
        binding.catalogTitlebar.backButton.setOnClickListener {
            navController.navigate(R.id.action_catalogFragment_to_homeFragment)
        }

        setupProductRecyclerView()
        setupCategoryRecyclerView()
        setupClickListeners()
        observeUiState()
    }

    private fun updateCategoryList(categories: List<Category>) {
        if (categories.isNotEmpty()) {
            categoryAdapter.submitList(categories)
            binding.categoryListRv.visibility = View.VISIBLE
        } else {
            binding.categoryListRv.visibility = View.GONE
        }
    }

    private fun updateProductList(products: List<Product>) {
        if (products.isNotEmpty()) {
            productAdapter.submitList(products)
            binding.productListRv.visibility = View.VISIBLE
        } else {
            binding.productListRv.visibility = View.GONE
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                catalogViewModel.uiState.collectLatest { uiState ->
                    if (uiState.errorMessage != null) {
                        Log.e("CatalogFragment", uiState.errorMessage)
                    }
                    updateCategoryList(uiState.categories)
                    updateProductList(uiState.products)
                    updateTotalCount(uiState.selectedProducts)
                }
            }
        }
    }

    private fun updateTotalCount(selectedProducts: Map<Product, Int>) {
        val totalCount = selectedProducts.values.sum()
        binding.orderItemCountTv.text = totalCount.toString()
        binding.proceedOrderLv.visibility = if (totalCount > 0) View.VISIBLE else View.GONE
        Log.d("CatalogFragment", "Total items selected: $totalCount")
    }

    private fun setupClickListeners() {
        binding.apply {
            categoryAdapter.onItemClick = { category ->
                catalogViewModel.onUiEvent(CatalogUiEvent.OnCategorySelected(category.id))
            }

            // Move the proceedOrderLv click listener out of the product click listener
            proceedOrderLv.setOnClickListener {
                val selectedProducts = catalogViewModel.uiState.value.selectedProducts.map {
                    (product,quantity) ->
                    SelectedItem(product, quantity)
                }.toTypedArray()

                navController.navigate(R.id.action_catalogFragment_to_billCreatedFragment, Bundle().apply {
                    putParcelableArray("selectedProducts", selectedProducts)
                })
            }
        }
    }

    private fun setupCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter()
        binding.categoryListRv.apply {
            adapter = categoryAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setupProductRecyclerView() {
        productAdapter = ProductAdapter().apply {
            onItemClick = { product, binding ->
                val selectedProducts = catalogViewModel.uiState.value.selectedProducts
                val currentCount = selectedProducts[product] ?: 0
                val position = productAdapter.currentList.indexOf(product)
                if (currentCount == 0) {
                    catalogViewModel.onUiEvent(CatalogUiEvent.ProductCountChanged(product, 1))
                } else {
                    catalogViewModel.onUiEvent(CatalogUiEvent.ProductCountChanged(product, 0))
                }
                productAdapter.notifyItemChanged(position)
            }

            onIncrementClick = { product, position ->
                val selectedProducts = catalogViewModel.uiState.value.selectedProducts
                val currentCount = selectedProducts[product] ?: 0
                catalogViewModel.onUiEvent(CatalogUiEvent.ProductCountChanged(product, currentCount + 1))
                productAdapter.notifyItemChanged(position)
            }

            onDecrementClick = { product, position ->
                val selectedProducts = catalogViewModel.uiState.value.selectedProducts
                val currentCount = selectedProducts[product] ?: 0
                if (currentCount > 1) {
                    catalogViewModel.onUiEvent(CatalogUiEvent.ProductCountChanged(product, currentCount - 1))
                } else {
                    catalogViewModel.onUiEvent(CatalogUiEvent.ProductCountChanged(product, 0))
                }
                productAdapter.notifyItemChanged(position)
            }

            onQuantityChanged = { product, quantity, position ->
                // Remove any pending updates
                runnable?.let { handler.removeCallbacks(it) }

                // Create a new update task
                runnable = Runnable {
                    if (quantity > 0) {
                        catalogViewModel.onUiEvent(CatalogUiEvent.ProductCountChanged(product, quantity))
                    } else {
                        catalogViewModel.onUiEvent(CatalogUiEvent.ProductCountChanged(product, 0))
                    }
                    productAdapter.notifyItemChanged(position)
                }
                // Delay the update by 300ms to debounce rapid changes
//                handler.postDelayed(runnable!!, 300)
            }

            updateSelectionStateProvider { product ->
                val selectedProducts = catalogViewModel.uiState.value.selectedProducts
                val quantity = selectedProducts[product] ?: 0
                SelectionState(isSelected = quantity > 0, quantity = quantity)
            }
        }

        binding.productListRv.apply {
            adapter = productAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}