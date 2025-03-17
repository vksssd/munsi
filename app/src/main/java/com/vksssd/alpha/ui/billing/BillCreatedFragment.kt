package com.vksssd.alpha.ui.billing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.vksssd.alpha.R
import com.vksssd.alpha.data.entity.Bill
import com.vksssd.alpha.data.entity.BillStatus
import com.vksssd.alpha.data.entity.BillType
import com.vksssd.alpha.databinding.FragmentBillCreatedBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class BillCreatedFragment : Fragment() {

    private var _binding: FragmentBillCreatedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BillCreatedViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBillCreatedBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current amount (as a String)
        outState.putString("amount_text", binding.totalAmountValueTv.text.toString())
        outState.putString("amount_adjusted_text", binding.totalAdjustAmountEt.text.toString())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            val amountText = savedInstanceState.getString("amount_text")
            binding.totalAmountValueTv.text = amountText
            val adjustedAmountText = savedInstanceState.getString("amount_adjusted_text")
            binding.totalAdjustAmountEt.setText( adjustedAmountText)
        }


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

            continuePaymentTv.setOnClickListener {
                if (orderConfirmationCardCl.isVisible) {
                    orderConfirmationCardCl.visibility = View.GONE
                } else {
                    orderConfirmationCardCl.visibility = View.VISIBLE
                    continuePaymentTv.visibility = View.INVISIBLE
                    cartItems.isClickable = false
                    cartItems.isFocusable = false
                    cartItems.isFocusableInTouchMode = false
                    cartItems.isEnabled = false

                    // Get the amount from the TextView
                    val amountText = totalAmountValueTv.text.toString()
                    val amount = amountText.toDoubleOrNull()

                    if (amount != null) {
                        // Create the Bill object with the parsed amount
                        val newBill = Bill(
                            customerName = "New Customer",
                            billType = BillType.DIRECT_CASH,
                            billDate = Date(),
                            billAmount = amount, // Use the parsed Double
                            billFinalAmount = amount,
                            discount = 0.0,
                            status = BillStatus.PAID
                        )
                        viewModel.addBill(newBill)
                    } else {
                        // Handle the case where the amount is invalid
                        Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
                    }
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