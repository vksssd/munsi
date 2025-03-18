package com.vksssd.alpha.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/** Represents a product category in the database. */
@Parcelize
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryName: String,
    val isActive: Boolean = true,
    val inStock: Boolean = true,
    val imageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis()
) : Parcelable

/** Represents a product in the database, linked to a category. */
@Parcelize
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
    val categoryId: Long,
    val stockQuantity: Int = 0,
    val imageUrl: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis()
) : Parcelable

/** Represents a selected product with a quantity, used for passing data between components. */
@Parcelize
data class SelectedItem(
    val product: Product,
    val quantity: Int
) : Parcelable