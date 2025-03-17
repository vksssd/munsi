package com.vksssd.alpha.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryName: String,
//    val description: String,
    val isActive: Boolean? = true,
    val inStock: Boolean? = true,
    val imageUrl : String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productName: String,
    val description: String,
    val price: Double,
    val categoryId: Long, // Foreign key to Category
    val stockQuantity: Int ?= 0,
    val imageUrl : String? = null,
    val isActive: Boolean = true
)