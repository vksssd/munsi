package com.vksssd.alpha.ui.inventory

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vksssd.alpha.data.entity.Category
import com.vksssd.alpha.data.entity.Product
import com.vksssd.alpha.databinding.ItemCatalogBinding

data class SelectionState(val isSelected: Boolean, val quantity: Int)

class ProductAdapter: ListAdapter<Product, ProductAdapter.NewProductViewModel>(InventoryDiffCallback())  {

    var onItemClick: ((Product, ItemCatalogBinding) -> Unit)? = null
    var onIncrementClick: ((Product, Int) -> Unit)? = null
    var onDecrementClick: ((Product, Int) -> Unit)? = null
    var onQuantityChanged: ((Product, Int, Int) -> Unit)? = null // product, quantity, position
    var selectionStateProvider: ((Product) -> SelectionState)? = null


    fun updateSelectionStateProvider(provider: (Product) -> SelectionState) {
        this.selectionStateProvider = provider
        notifyDataSetChanged()
    }


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


    inner class NewProductViewModel(val binding: ItemCatalogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            val selectionState = selectionStateProvider?.invoke(item) ?: SelectionState(false, 0)
            binding.apply {
                foodTitleTextview.text = item.productName
                foodPriceTextview.text = item.price.toString()
                foodSmallpriceTextview.text = item.price.toString() + " /qt"

                if (selectionState.isSelected) {
                    itemCountLv.visibility = View.VISIBLE
                    itemCountEt.setText(selectionState.quantity.toString())
                    foodSmallpriceTextview.visibility = View.VISIBLE
                    foodPriceTextview.visibility = View.GONE
                } else {
                    itemCountLv.visibility = View.GONE
                    foodPriceTextview.visibility = View.VISIBLE
                    foodSmallpriceTextview.visibility = View.GONE
                }

                // Set click listeners using item and adapterPosition
                root.setOnClickListener { onItemClick?.invoke(item, this) }
                addItemCountIv.setOnClickListener { onIncrementClick?.invoke(item, adapterPosition) }
                removeItemCountIv.setOnClickListener { onDecrementClick?.invoke(item, adapterPosition) }

                // TextWatcher for itemCountEt
                itemCountEt.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        val quantity = s.toString().toIntOrNull() ?: 0
                        onQuantityChanged?.invoke(item, quantity, adapterPosition)
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })
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