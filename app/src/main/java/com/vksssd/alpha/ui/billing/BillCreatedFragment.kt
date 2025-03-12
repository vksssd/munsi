package com.vksssd.alpha.ui.billing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.vksssd.alpha.R
import com.vksssd.alpha.databinding.FragmentBillCreatedBinding
import com.vksssd.alpha.databinding.FragmentCashinBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener

/**
 * A simple [Fragment] subclass.
 * Use the [BillCreatedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class BillCreatedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentBillCreatedBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBillCreatedBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chckoutTitlebar.toolbarTitle.text = "Billing"
        binding.chckoutTitlebar.searchButton.visibility = View.INVISIBLE
        binding.chckoutTitlebar.backButton.visibility = View.VISIBLE
        binding.orderConfirmationCardCl.visibility=View.GONE
        binding.continuePaymentTv.visibility = View.VISIBLE
        setupFragmentResultListeners()


        setupClickListeners()
        observeViewModel()
    }


    private fun setupFragmentResultListeners() {
        setFragmentResultListener("requestKey"){ requestKey, bundle ->
            val amount = bundle.getString("amount_cashin")
            binding.totalAmountValueTv.text = amount
            amount?.let{
                binding.totalAdjustAmountEt.setText(it)
            }
            binding.totalItemsTv.visibility = View.INVISIBLE
            binding.totalCategoryTv.visibility = View.INVISIBLE
            binding.totalItemCountTv.visibility = View.INVISIBLE
            binding.totalCategoryValueTv.visibility = View.INVISIBLE
            binding.cartItems.visibility = View.INVISIBLE
        }
    }


    private fun setupClickListeners() {
        binding.apply {
            backBillCreatedTv.setOnClickListener {
                navController.navigate(R.id.action_billCreatedFragment_to_cashinFragment)
            }

            continuePaymentTv.setOnClickListener{
                if (orderConfirmationCardCl.isVisible) {
                    orderConfirmationCardCl.visibility = View.GONE
                } else {
                    orderConfirmationCardCl.visibility = View.VISIBLE
                    continuePaymentTv.visibility = View.INVISIBLE
                    cartItems.isClickable = false
                    cartItems.isFocusable = false
                    cartItems.isFocusableInTouchMode = false
                    cartItems.isEnabled = false
                }
            }

            backToHomeTv.setOnClickListener{
                navController.navigate(R.id.action_billCreatedFragment_to_homeFragment)
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