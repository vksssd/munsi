package com.vksssd.alpha.ui.settings

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.vksssd.alpha.R
import com.vksssd.alpha.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
//    private var recyclerViewState: Parcelable? = null
    private var scrollViewState: Int ?= null
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        navController = findNavController()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initializeUI(view)
//        setupRecyclerView()

        binding.apply {
            profileTitlebar.toolbarTitle.text = "Profile"

//         settings
            languageSetting.itemTitleTv.text = "Language"
            languageSetting.itemSubtextIdTv.text ="Choosen Language: English"

            permissionSetting.itemTitleTv.text = "Permission"
            permissionSetting.itemSubtextIdTv.text ="Manage data sharing settings"

            themeSetting.itemTitleTv.text = "Theme"
            themeSetting.itemSubtextIdTv.text = "Choose theme between light and dark mode"


//            privacy and security settings
            lockScreenSettingTab.itemTitleTv.text = "Lock Screen"
            lockScreenSettingTab.itemSubtextIdTv.text = "Biometric & Screen locks"
            changePasswordSettingTab.itemTitleTv.text ="Change Password"
            changePasswordSettingTab.itemSubtextIdTv.text ="Change your password"

        }

        setupClickListeners()
        observeViewModel()
    }


    private fun setupClickListeners() {
        // Set click listeners for UI elements
        binding.apply {

            profileCard.setOnClickListener {
                navController.navigate(R.id.action_profileFragment_to_editProfileFragment)
            }

            profileTitlebar.backButton.setOnClickListener{
                navController.navigate(R.id.action_profileFragment_to_home_nav)
            }

            receiveMoneyCard.setOnClickListener {
                navController.navigate(R.id.action_profileFragment_to_receiveMoneyFragment)
            }

            allPaymentReceiveAppCard.viewMoreText.setOnClickListener{
                navController.navigate(R.id.action_profileFragment_to_paymentAppsFragment)
            }

            aboutAppCard.setOnClickListener{
                navController.navigate(R.id.action_profileFragment_to_aboutFragment)
            }

            languageSetting.root.setOnClickListener{
                navController.navigate(R.id.action_profileFragment_to_languageFragment)
            }

            permissionSetting.root.setOnClickListener{
                navController.navigate(R.id.action_profileFragment_to_permissionFragment)
            }

            themeSetting.root.setOnClickListener{
                navController.navigate(R.id.action_profileFragment_to_themeFragment)
            }

//            lockScreenSettingTab.root.setOnClickListener{
//                navController.navigate(R.id.action_profileFragment_to_passwordFragment)
//            }
//
            changePasswordSettingTab.root.setOnClickListener{
                navController.navigate(R.id.action_profileFragment_to_passwordFragment)
            }




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