package com.vksssd.alpha.ui.billing.cart

import android.os.Bundle
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
import com.vksssd.alpha.data.entity.Product
import com.vksssd.alpha.databinding.FragmentCatalogBinding
import com.vksssd.alpha.ui.inventory.CategoryAdapter
import com.vksssd.alpha.ui.inventory.CategoryViewModel
import com.vksssd.alpha.ui.inventory.ProductAdapter
import com.vksssd.alpha.ui.inventory.ProductViewModel
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

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()

    // Keep track of the selected products and their quantities
    private val selectedProducts = mutableMapOf<Product, Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
        setupViewModelObservers()

        categoryAdapter.onItemClick = { category ->
            productViewModel.getProductByCategoryId(category.id)
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            productAdapter.onItemClick = { product, productItemBinding ->
                // Toggle visibility of item_count_lv, open_group_imageview, and foodPriceTextview
                if (productItemBinding.itemCountLv.visibility == View.GONE) {
                    productItemBinding.itemCountLv.visibility = View.VISIBLE
                    productItemBinding.openGroupImageview.visibility = View.GONE
                    productItemBinding.foodPriceTextview.visibility = View.GONE

                    // Add the product to selectedProducts with a default quantity of 1
                    selectedProducts[product] = 1

                    // Update total count
                    updateTotalCount()
                } else {
                    productItemBinding.itemCountLv.visibility = View.GONE
                    productItemBinding.openGroupImageview.visibility = View.GONE
                    productItemBinding.foodPriceTextview.visibility = View.VISIBLE


                    // Remove the product if the count view is hidden
                    selectedProducts.remove(product)

                    // Update total count
                    updateTotalCount()
                }

                // Update item count
                var currentCount = productItemBinding.itemCountEt.text.toString().toInt()
                productItemBinding.addItemCountIv.setOnClickListener {
                    currentCount++
                    productItemBinding.itemCountEt.setText(currentCount.toString())

                    // Update the quantity in the selectedProducts map
                    selectedProducts[product] = currentCount

                    // Update total count
                    updateTotalCount()
                }

                productItemBinding.removeItemCountIv.setOnClickListener {
                    if (currentCount > 1) {
                        currentCount--
                        productItemBinding.itemCountEt.setText(currentCount.toString())

                        // Update the quantity in the selectedProducts map
                        selectedProducts[product] = currentCount
                    }else if (currentCount <= 1) {
                        // Unselect the product by hiding count view and showing original views
                        productItemBinding.itemCountLv.visibility = View.GONE
                        productItemBinding.openGroupImageview.visibility = View.GONE
                        productItemBinding.foodPriceTextview.visibility = View.VISIBLE

                        // Remove the product from selectedProducts
                        selectedProducts.remove(product)
                    }
                        updateTotalCount()
                }


                // Show proceedOrderLv based on whether items are selected
                if (getTotalItemCount() > 0) {
                    proceedOrderLv.visibility = View.VISIBLE


                } else {
                    proceedOrderLv.visibility = View.GONE
                }

                proceedOrderLv.setOnClickListener {
                    navController.navigate(R.id.action_catalogFragment_to_billCreatedFragment)
                }
            }
        }
    }

    // Helper function to calculate and update the total count
    private fun updateTotalCount() {
        val totalCount = getTotalItemCount()
        binding.orderItemCountTv.text = totalCount.toString()

        // Update visibility of proceed button
        if (totalCount > 0) {
            binding.proceedOrderLv.visibility = View.VISIBLE
        } else {
            binding.proceedOrderLv.visibility = View.GONE
        }

        Log.d("CatalogFragment", "Total items selected: $totalCount")
    }

    // Helper function to calculate the total number of items
    private fun getTotalItemCount(): Int {
        return selectedProducts.values.sum()
    }

    private fun setupCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter()
        binding.categoryListRv.apply {
            adapter = categoryAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupProductRecyclerView() {
        productAdapter = ProductAdapter()
        binding.productListRv.apply {
            adapter = productAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupViewModelObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe categories
                launch {
                    categoryViewModel.categories.collectLatest { categories ->
                        Log.d("CatalogFragment", "categories collected, size: ${categories.size}")
                        if (categories.isNotEmpty()) {
                            categoryAdapter.submitList(categories)
                            binding.categoryListRv.visibility = View.VISIBLE
                        } else {
                            binding.categoryListRv.visibility = View.GONE
                        }
                    }
                }
                // Observe products returned by getProductByCategoryId
                launch {
                    productViewModel.products.collectLatest { products ->
                        Log.d("CatalogFragment", "products collected, size: ${products.size}")
                        if (products.isNotEmpty()) {
                            productAdapter.submitList(products)
                            binding.productListRv.visibility = View.VISIBLE
                        } else {
                            binding.productListRv.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        // Perform actions when the fragment is visible
    }


    override fun onStop() {
        super.onStop()
        // Perform actions when the fragment is no longer visible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up any non-view related resources
    }
}