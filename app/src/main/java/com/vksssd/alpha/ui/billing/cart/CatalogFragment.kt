package com.vksssd.alpha.ui.billing.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.vksssd.alpha.R
import com.vksssd.alpha.databinding.FragmentCatalogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatalogFragment : Fragment() {
    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!
    private var scrollViewState: Int? = null
    private lateinit var navController: NavController

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
        binding.catalogTitlebar.searchButton.visibility = View.INVISIBLE
        binding.catalogTitlebar.backButton.visibility = View.VISIBLE
        binding.catalogTitlebar.backButton.setOnClickListener {
            navController.navigate(R.id.action_catalogFragment_to_homeFragment)
        }

        observeViewModel()
    }



    private fun setupClickListeners() {
        // Set click listeners for UI elements
    }

    private fun setupRecyclerView() {
        // Set up RecyclerView
    }

    private fun observeViewModel() {
        // Observe LiveData from ViewModel
    }

//    private fun updateHistoryList(historyItems: List<HistoryItem>) {
//        // Update the history list in the RecyclerView
//        historyAdapter.submitList(historyItems)
//        viewAllHistory.findViewById<TextView>(R.id.card_title).text = "Recent Transactions"
//    }

    override fun onStart() {
        super.onStart()
        // Perform actions when the fragment is visible
    }


    override fun onResume() {
        super.onResume()
        if (scrollViewState != null) {
            binding.nestedScrollView.scrollY = scrollViewState!!
        }
        // Perform actions when the fragment is in the foreground
    }

    override fun onPause() {
        super.onPause()
        // Save the scroll state
//        recyclerViewState = binding.settingsRecyclerView.layout_marginager?.onSaveInstanceState()
        scrollViewState = binding.nestedScrollView.scrollY
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