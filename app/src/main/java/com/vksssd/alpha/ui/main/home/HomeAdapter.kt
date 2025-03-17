package com.vksssd.alpha.ui.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vksssd.alpha.data.entity.Transaction
import com.vksssd.alpha.data.entity.TransactionType
import com.vksssd.alpha.databinding.ItemTilePaymentBinding
import com.vksssd.alpha.R


class HomeAdapter : ListAdapter<Transaction, HomeAdapter.HomeViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemTilePaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<Transaction>?) {
        Log.d("HomeAdapter", "submitList() called with list size: ${list?.size}")
        super.submitList(list)
    }

    class HomeViewHolder(private val binding: ItemTilePaymentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Transaction?) {
            binding.apply {
                    paymentAmountTv.text = item?.amount.toString()
                    val imageSrc = when(item?.type){
                        TransactionType.CASH_IN -> R.drawable.ic_cash
                        TransactionType.UPI_PAYMENT -> R.drawable.ic_amazon
                        TransactionType.BILL_PAYMENT -> R.drawable.ic_cart
                        TransactionType.REFUND -> R.drawable.ic_wallet
                        null -> TODO()
                    }
                    paymentModeIcon.setImageResource(imageSrc)
            }
        }
    }

    private class TransactionDiffCallback: DiffUtil.ItemCallback<Transaction>() {

        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.transactionId == newItem.transactionId
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }

    }

}