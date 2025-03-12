package com.vksssd.alpha.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val upiId: String,
    val address: String,
    val profilePictureUrl: String? = null,
    val currency: String = "USD",
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
//    val permissions: List<String> ?= null,
//    val apps: List<String>
)