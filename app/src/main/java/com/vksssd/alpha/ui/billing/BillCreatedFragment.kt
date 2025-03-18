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
import androidx.recyclerview.widget.LinearLayoutManager
import com.vksssd.alpha.data.entity.Bill
import com.vksssd.alpha.data.entity.BillStatus
import com.vksssd.alpha.data.entity.BillType
import com.vksssd.alpha.data.entity.SelectedItem
import com.vksssd.alpha.databinding.FragmentBillCreatedBinding
import com.vksssd.alpha.ui.inventory.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class BillCreatedFragment : Fragment() {

    private var _binding: FragmentBillCreatedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BillCreatedViewModel by viewModels()

    private lateinit var productAdapter: ProductAdapter


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

        productAdapter = ProductAdapter()

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
        binding.chckoutTitlebar.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.backToHomeTv.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.backBillCreatedTv.setOnClickListener {
            requireActivity().onBackPressed()
        }


        val selectedProducts: Array<SelectedItem>? =
            arguments?.getParcelableArray("selectedProducts") as? Array<SelectedItem>

        if (selectedProducts != null) {
            // Calculate the total price efficiently.
            val total = selectedProducts.sumOf { it.product.price * it.quantity }
            // Update visibility of views.
            binding.cartItemsRv.visibility = View.VISIBLE
            binding.totalItemsTv.visibility = View.VISIBLE
            binding.totalProductCountTv.visibility = View.VISIBLE
            binding.totalCategoryTv.visibility = View.VISIBLE
            binding.totalItemCountTv.visibility = View.VISIBLE
            binding.totalCategoryValueTv.visibility = View.VISIBLE
            binding.totalAmountValueTv.visibility = View.VISIBLE
            binding.totalAdjustAmountEt.visibility = View.VISIBLE
            binding.continuePaymentTv.visibility = View.VISIBLE


            val uniqueCategories = selectedProducts.map { it.product.categoryId }.distinct()
            // Set text values for each view
            binding.totalItemCountTv.text = selectedProducts.sumOf {it.quantity}.toString()
            binding.totalProductCountTv.text = selectedProducts.size.toString()
            binding.totalCategoryValueTv.text = uniqueCategories.size.toString()
            binding.totalAmountValueTv.text = total.toString()
            binding.totalAdjustAmountEt.setText(total.toString())

            productAdapter.submitList(selectedProducts.map { it.product })
        } else {
            println("selectedProducts is null")
        }


        setupFragmentResultListeners()
        setupClickListeners()
        setupRecyclerView()
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
            binding.cartItemsRv.visibility = View.INVISIBLE
        }
    }


    private fun setupClickListeners() {
        binding.apply {

            continuePaymentTv.setOnClickListener {
                if (orderConfirmationCardCl.isVisible) {
                    orderConfirmationCardCl.visibility = View.GONE
                } else {
                    orderConfirmationCardCl.visibility = View.VISIBLE
                    continuePaymentTv.visibility = View.INVISIBLE
                    cartItemsRv.isClickable = false
                    cartItemsRv.isFocusable = false
                    cartItemsRv.isFocusableInTouchMode = false
                    cartItemsRv.isEnabled = false

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


        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter()
        binding.cartItemsRv.apply {
            adapter = productAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
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