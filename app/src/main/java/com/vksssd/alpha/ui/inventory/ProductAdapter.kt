package com.vksssd.alpha.ui.inventory

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vksssd.alpha.data.entity.Category
import com.vksssd.alpha.data.entity.Product
import com.vksssd.alpha.databinding.ItemCatalogBinding

class ProductAdapter: ListAdapter<Product, ProductAdapter.NewProductViewModel>(InventoryDiffCallback())  {

    var onItemClick: ((Product, ItemCatalogBinding) -> Unit)? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewProductViewModel {
//        val binding = ItemCatalogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val binding = ItemCatalogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewProductViewModel(binding)
    }


    override fun onBindViewHolder(holder: NewProductViewModel, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<Product>?) {
        Log.d("InventoryAdapter", "submitList() called with list size: ${list?.size}")
        super.submitList(list)
    }


    inner class NewProductViewModel(private val binding: ItemCatalogBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.apply {
                foodTitleTextview.text = item.productName
                foodPriceTextview.text = item.price.toString()
                foodSmallpriceTextview.text = item.price.toString()+" /qt"
//                Log.d("InventoryAdapter", "category image url" + item?.imageUrl)
//                categoryItemImage.setImageResource(item?.imageUrl?.toInt() ?: R.drawable.ic_category2)
            }

            itemView.setOnClickListener {
                onItemClick?.invoke(item, binding)
            }
        }


    }

    class InventoryDiffCallback: DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

}