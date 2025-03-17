package com.vksssd.alpha.ui.main.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vksssd.alpha.R
import com.vksssd.alpha.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            historyTitlebar.toolbarTitle.text = "History"
            historyTitlebar.backButton.visibility = View.VISIBLE
            historySearchbar.searchBar.visibility = View.GONE
        }
        setupClickListener()
        setupRecyclerView()
        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {
        // Using repeatOnLifecycle to ensure proper collection
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.transactions.collectLatest { categories ->
                        Log.d("HistoryFragment", "Transactions collected, size: ${categories.size}")
                        if (categories.isNotEmpty()){
                            adapter.submitList(categories)
                            binding.historyFragmentRv.visibility = View.VISIBLE
                        }else{
                            binding.historyFragmentRv.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter()
        binding.historyFragmentRv.apply {
            adapter = this@HistoryFragment.adapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClickListener() {
        binding.apply {
            historyTitlebar.backButton.setOnClickListener {
                navController.navigate(R.id.action_historyFragment_to_homeFragment)
            }
            historyTitlebar.searchButton.setOnClickListener {
                historySearchbar.searchBar.visibility =
                    if (historySearchbar.searchBar.visibility != View.VISIBLE) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}