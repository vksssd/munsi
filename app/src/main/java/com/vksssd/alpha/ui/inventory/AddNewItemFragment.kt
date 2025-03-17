package com.vksssd.alpha.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.vksssd.alpha.R
import com.vksssd.alpha.data.entity.Product
import com.vksssd.alpha.databinding.FragmentAddNewItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNewItemFragment : Fragment() {

    private var _binding: FragmentAddNewItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val viewModel : ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddNewItemBinding.inflate(inflater, container, false)
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addNewItemTitlebar.toolbarTitle.text = "Add Product"
        binding.addNewItemTitlebar.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.addNewItemTitlebar.searchButton.visibility = View.INVISIBLE
        binding.toastCard.toastCardBar.visibility = View.GONE


        binding.saveBtnTv.itemCard.visibility = View.VISIBLE
        binding.saveBtnTv.tileText.text = "Save"
        binding.saveBtnTv.tileIcon.setImageResource(R.drawable.ic_wallet)


        binding.resetBtnTv.itemCard.visibility = View.VISIBLE
        binding.resetBtnTv.tileText.text = "Reset"
        binding.resetBtnTv.tileIcon.setImageResource(R.drawable.ic_close)

        setupClickListeners()
//        observeViewModel()

    }

    private fun setupClickListeners() {
        binding.apply {

//            make it click or select image from gallery or camera
            productPictureValueIv.setOnClickListener {
                productPictureValueIv.setImageResource(R.drawable.ic_food)
            }


            saveBtnTv.itemCard.setOnClickListener {
                val name = binding.productNameValueTv.text.toString().trim()
                val price = binding.productPriceValueTv.text.toString().trim()
                val description = binding.productDescriptionValueTv.text.toString().trim()

                if (name.isEmpty() || price.isEmpty()) {
                    //update ui
                    binding.toastCard.toastCardBar.visibility = View.VISIBLE
                    binding.toastCard.toastCardText.text = "Product name or price cannot be empty"
                    val color = ContextCompat.getColor(requireContext(), R.color.card_background)
                    binding.toastCard.toastCardBar.setBackgroundColor(color)
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(1000) // Wait for 1 seconds
                        binding.toastCard.toastCardBar.visibility = View.GONE
                    }
                    return@setOnClickListener
                }

                viewModel.addNewProduct( product = Product(
                    productName = name,
                    price = price.toDouble(),
                    description = description,
                    categoryId = 1
                ))

                binding.toastCard.toastCardBar.visibility = View.VISIBLE
                binding.toastCard.toastCardText.text = "Product added successfully"

                binding.productNameValueTv.setText("")
                binding.productPriceValueTv.setText("")
                binding.productDescriptionTv.setText("")
                binding.productPictureValueIv.setImageResource(R.drawable.ic_camera)


                val color = ContextCompat.getColor(requireContext(), R.color.glass)
                binding.toastCard.toastCardBar.setBackgroundColor(color)

                // Use coroutine to delay the hide toastCard
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(1000) // Wait for 1 seconds
                    binding.toastCard.toastCardBar.visibility = View.GONE
                }

            }

            resetBtnTv.itemCard.setOnClickListener{
                binding.productNameValueTv.setText("")
                binding.productPriceValueTv.setText("")
                binding.productDescriptionValueTv.setText("")
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}