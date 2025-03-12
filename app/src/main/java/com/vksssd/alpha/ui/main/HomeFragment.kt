package com.vksssd.alpha.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vksssd.alpha.R
import com.vksssd.alpha.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var scrollViewState: Int ?= null
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupRecyclerView()
        binding.cashInCard.tileText.text = "Cash In"
        binding.cartCard.tileText.text= "Add In Cart"
        binding.cartCard.tileIcon.setImageResource(R.drawable.ic_cart)

        setupClickListeners()
        observeViewModel()
    }


    private fun setupClickListeners() {
        // Set click listeners for UI elements
        binding.apply {
            placeholderLineIncluded.graphTitle.setOnClickListener {
                navController.navigate(R.id.action_homeFragment_to_statsFragment)
            }

            homeNotificationIcon.setOnClickListener {
                navController.navigate(R.id.action_homeFragment_to_notificationFragment)
            }

            homeProfileIcon.setOnClickListener {
                navController.navigate(R.id.action_homeFragment_to_setting_nav)
            }

            viewAllCardView2.viewMoreText.setOnClickListener {
                navController.navigate(R.id.action_homeFragment_to_historyFragment)
            }
            cashInCard.root.setOnClickListener {
                navController.navigate(R.id.action_homeFragment_to_cashinFragment)
            }
            cartCard.root.setOnClickListener {
                navController.navigate(R.id.action_homeFragment_to_catalogFragment)
            }

            inventoryCard.root.setOnClickListener{
                navController.navigate(R.id.action_homeFragment_to_inventory_nav)
            }

            announceCard.root.setOnClickListener{
                navController.navigate(R.id.action_homeFragment_to_anounce_nav)
            }
        }
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