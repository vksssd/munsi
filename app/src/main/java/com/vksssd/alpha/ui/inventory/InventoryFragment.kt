package com.vksssd.alpha.ui.inventory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vksssd.alpha.R
import com.vksssd.alpha.databinding.FragmentInventoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class InventoryFragment : Fragment() {

    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inventoryTitlebar.toolbarTitle.text = "Inventory"
        binding.inventoryTitlebar.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.inventorySearchbar.searchBar.visibility = View.GONE
        binding.addProduct.visibility = View.GONE
        binding.addCategory.visibility = View.GONE
        binding.closeAddProductFab.setImageResource(R.drawable.ic_add)

        setupClickListeners()
        setupCategoryRecyclerView()
        setupProductRecyclerView()
        setupViewModelObservers()

        // Set the category item click callback
        categoryAdapter.onItemClick = { category ->
            productViewModel.getProductByCategoryId(category.id)
        }
    }

    private fun setupCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter()
        binding.categoryListRv.apply {
            adapter = categoryAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
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
                        Log.d("InventoryFragment", "categories collected, size: ${categories.size}")
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
                        Log.d("InventoryFragment", "products collected, size: ${products.size}")
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

    private fun setupClickListeners() {
        binding.apply {
            inventoryTitlebar.searchButton.setOnClickListener {
                inventorySearchbar.searchBar.visibility =
                    if (inventorySearchbar.searchBar.isGone) View.VISIBLE else View.GONE
            }
            addCategory.setOnClickListener {
                navController.navigate(R.id.action_inventoryFragment_to_newCategoryFragment)
            }
            addProduct.setOnClickListener {
                navController.navigate(R.id.action_inventoryFragment_to_addNewItemFragment)
            }
            closeAddProductFab.setOnClickListener {
                if (addProduct.isGone && addCategory.isGone) {
                    closeAddProductFab.setImageResource(R.drawable.ic_close)
                    addCategory.visibility = View.VISIBLE
                    addProduct.visibility = View.VISIBLE
                } else {
                    closeAddProductFab.setImageResource(R.drawable.ic_add)
                    addCategory.visibility = View.GONE
                    addProduct.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
