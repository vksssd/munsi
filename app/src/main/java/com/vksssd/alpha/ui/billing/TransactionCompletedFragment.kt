package com.vksssd.alpha.ui.billing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.vksssd.alpha.R
import com.vksssd.alpha.databinding.FragmentCashinBinding

class TransactionCompletedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentCashinBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCashinBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cashinTitlebar.toolbarTitle.text = "Cash In"
        binding.cashinTitlebar.searchButton.visibility = View.INVISIBLE
        binding.cashinTitlebar.backButton.visibility = View.VISIBLE
        binding.cashinTitlebar.backButton.setOnClickListener {
            navController.navigate(R.id.action_cashinFragment_to_homeFragment)
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
        // Perform actions when the fragment is in the foreground
    }

    override fun onPause() {
        super.onPause()
        // Perform actions when the fragment is going into the background
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