package com.vksssd.alpha.ui.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.vksssd.alpha.R
import com.vksssd.alpha.data.entity.Category
import com.vksssd.alpha.databinding.FragmentNewCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewCategoryFragment : Fragment() {

    private var _binding: FragmentNewCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
//    private val viewModel: NewCategoryViewModel by viewModels()

    private val viewModel : CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewCategoryBinding.inflate(inflater, container, false)
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newCategoryTitlebar.toolbarTitle.text = "New Category"
        binding.newCategoryTitlebar.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.newCategoryTitlebar.searchButton.visibility = View.INVISIBLE

        binding.radioAvailable.isChecked = true
        binding.radioNotAvailable.isChecked = false
        binding.toastCard.toastCardBar.visibility = View.GONE

        binding.saveBtnTv.itemCard.visibility = View.VISIBLE
        binding.saveBtnTv.tileText.text = "Save"
        binding.saveBtnTv.tileIcon.setImageResource(R.drawable.ic_wallet)

        if(binding.radioAvailable.isChecked){
            binding.radioAvailable.isChecked = true
            binding.radioNotAvailable.isChecked = false
        } else{
            binding.radioAvailable.isChecked = false
            binding.radioNotAvailable.isChecked = true
        }

        binding.resetBtnTv.itemCard.visibility = View.VISIBLE
        binding.resetBtnTv.tileText.text = "Reset"
        binding.resetBtnTv.tileIcon.setImageResource(R.drawable.ic_close)

        setupClickListeners()
//        observeViewModel()

    }

    private fun setupClickListeners() {
        binding.apply {

//            make it click or select image from gallery or camera
            categoryAddImageIv.setOnClickListener {
                categoryAddImageIv.setImageResource(R.drawable.ic_category2)
            }


            saveBtnTv.itemCard.setOnClickListener {
                val categoryName = binding.categoryName.text.toString().trim()

                if (categoryName.isEmpty()) {
                    //update ui
                    binding.toastCard.toastCardBar.visibility = View.VISIBLE
                    binding.toastCard.toastCardText.text = "Category name cannot be empty"
                    val color = ContextCompat.getColor(requireContext(), R.color.card_background)
                    binding.toastCard.toastCardBar.setBackgroundColor(color)
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(1000) // Wait for 1 seconds
                        binding.toastCard.toastCardBar.visibility = View.GONE
                    }
                    return@setOnClickListener
                }

                val newCategory = Category(
                    categoryName = categoryName,
                    isActive = binding.radioAvailable.isChecked,
                    inStock = binding.radioAvailable.isChecked,
                    imageUrl = binding.categoryAddImageIv.toString(),
                )

                viewModel.addNewCategory(newCategory)

                //update ui
                binding.toastCard.toastCardBar.visibility = View.VISIBLE
                binding.toastCard.toastCardText.text = "Category added successfully"

                binding.categoryName.setText("")
                binding.categoryAddImageIv.setImageResource(R.drawable.ic_camera)
                binding.radioAvailable.isChecked = true
                binding.radioNotAvailable.isChecked = false

                val color = ContextCompat.getColor(requireContext(), R.color.glass)
                binding.toastCard.toastCardBar.setBackgroundColor(color)

                // Use coroutine to delay the hide toastCard
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(1000) // Wait for 1 seconds
                    binding.toastCard.toastCardBar.visibility = View.GONE
                }
            }

            resetBtnTv.itemCard.setOnClickListener{
                binding.categoryName.setText("")
                binding.radioAvailable.isChecked = true
                binding.radioNotAvailable.isChecked = false
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}